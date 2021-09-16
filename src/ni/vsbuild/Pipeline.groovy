package ni.vsbuild

import ni.vsbuild.stages.*

class Pipeline implements Serializable {

   private static final String JSON_FILE = 'build.json'

   private static final String MANIFEST_FILE = 'Built/installer/manifest.json'

   def script
   PipelineInformation pipelineInformation
   String jsonConfig
   def changedFiles

   int checkoutTimeoutInMinutes = 10

   static class Builder implements Serializable {

      def script
      BuildConfiguration buildConfiguration
      LabviewBuildVersion lvVersion
      String manifestFile
      def changedFiles
      def stages = []

      Builder(def script, BuildConfiguration buildConfiguration, LabviewBuildVersion lvVersion, String manifestFile, def changedFiles) {
         this.script = script
         this.buildConfiguration = buildConfiguration
         this.lvVersion = lvVersion
         this.manifestFile = manifestFile
         this.changedFiles = changedFiles
      }

      def withCodegenStage() {
         stages << new Codegen(script, buildConfiguration, lvVersion)
      }

      def withBuildStage() {
         stages << new Build(script, buildConfiguration, lvVersion)
      }

      def withTestStage() {
         stages << new Test(script, buildConfiguration, lvVersion)
      }

      def withPackageStage() {
         def packageStage = new Package(script, buildConfiguration, lvVersion)

         if (shouldBuildPackage(packageStage)) {
            stages << packageStage
         }
      }

      def withArchiveStage() {
         stages << new Archive(script, buildConfiguration, lvVersion, manifestFile)
      }

      def shouldBuildPackage(def packageStage) {
         // The plan is to enable automatic merging from main to
         // release or hotfix branch packages and not build packages
         // for any other branches, including main. The version must
         // be appended to the release or hotfix branch name after a
         // dash (-) or slash (/).
         def branchNameMatches = buildConfiguration.packageInfo &&
            (script.env.BRANCH_NAME.startsWith("release") ||
            script.env.BRANCH_NAME.startsWith("hotfix"))
         if (branchNameMatches) {
            return true
         }

         // We build packages for pull requests which modify files related
         // to package building. Without this, testing these changes either
         // requires the user to replay a previous build and force a package
         // build, or to submit the changes and hope that they were correct.
         // Since the build is exported as a pull request, other tooling
         // shouldn't be checking for the package output.
         if (script.env.CHANGE_ID) {
            def configurationFiles = ["build.toml"] as Set
            for (def pkg : packageStage.getPackages()) {
               for (def file : pkg.getConfigurationFiles()) {
                  configurationFiles.add(file.toLowerCase())
               }
            }

            for (def file : changedFiles) {
               if (file.toLowerCase() in configurationFiles) {
                  script.echo "Running package stage because file \"${file}\" was changed."
                  return true
               }
            }
         }

         return false
      }

      def buildPipeline() {
         if(buildConfiguration.codegen || buildConfiguration.projects) {
            withCodegenStage()
         }

         if(buildConfiguration.build) {
            withBuildStage()
         }

         if(buildConfiguration.test){
            withTestStage()
         }

         if(buildConfiguration.packageInfo) {
            withPackageStage()
         }

         if(buildConfiguration.archive) {
            withArchiveStage()
         }

         return stages
      }
   }

   Pipeline(script, PipelineInformation pipelineInformation) {
      this.script = script
      this.pipelineInformation = pipelineInformation
   }

   void execute() {
      try {
         def commit = readBuildInformation()

         def configuration = getArbitraryVersionConfiguration()
         def rebuild = true
         if (configuration.archive) {
            def archiveParentLocation = Archive.calculateArchiveParentLocation(script, configuration)
            script.node(pipelineInformation.nodeLabel) {
               validateShouldBuildPipeline(archiveParentLocation, configuration)
               rebuild = checkIfRebuildNeeded(archiveParentLocation, commit)
            }
         }
         if (rebuild) {

            // build dependencies before starting this pipeline
            script.buildDependencies(pipelineInformation)

            runBuild()
            validateBuild()
         }
      }
      finally {
         sendNotification()
      }
   }

   protected void executeStages(stages) {
      for (Stage stage : stages) {
         try {
            stage.execute()
         } catch (err) {
            script.failBuild(err.getMessage())
         }
      }
   }

   private def readBuildInformation() {
      def manifest = script.readJSON text: '{}'
      def config
      script.node(pipelineInformation.nodeLabel) {
         script.stage('Checkout_readBuildInformation') {
            script.deleteDir()
            script.echo 'Attempting to get source from repo.'
            script.timeout(time: checkoutTimeoutInMinutes, unit: 'MINUTES'){
               manifest['scm'] = script.checkout(script.scm)
            }
         }
         script.stage('Setup_readBuildInformation') {
            script.cloneBuildTools()
            script.toml2json()

            config = script.readJSON file: JSON_FILE
            if (script.env.CHANGE_ID) {
               changedFiles = script.getChangedFiles()
            }
         }
      }

      jsonConfig = config.toString()
      return manifest['scm']['GIT_COMMIT']
   }

   private void validateShouldBuildPipeline(archiveParentLocation, configuration) {
      // We do not want to rebuild if our output would clobber existing data.
      // This can happen if the Jenkins build numbers reset, or e.g. due to
      // multiple repositories unintentionally exporting to the same location.
      def archiveLocation = Archive.calculateArchiveLocation(script, configuration)
      script.stage('validateShouldBuildPipeline') {
         if (script.fileExists(archiveParentLocation + "\\norebuild")) {
            script.failBuild("Refusing to build, norebuild file exists.")
         }

         if (script.fileExists(archiveLocation)) {
            script.failBuild("Refusing to build, $archiveLocation already exists and would be overwritten.")
         }
      }
   }

   private def checkIfRebuildNeeded(archiveParentLocation, commit) {
      script.stage('checkIfRebuildNeeded') {
         // If build was not caused by an upstream job, i.e. it was started manually
         // or because of a source change, always build it.
         // Note that the Jenkins CI lists this method as EXPERIMENTAL, but it has worked for many years.
         def upstreamBuild = script.currentBuild.getBuildCauses('hudson.model.Cause$UpstreamCause')
         if (!upstreamBuild) {
            return true
         }

         // If build was started by an upstream job, check if there have been any
         // changes to the repo since the last successful build.
         // If there are changes, do the build.
         def lastBuildLocation = script.findLatestDirectory(archiveParentLocation)
         def rebuild = script.needsRebuild(lastBuildLocation, commit, pipelineInformation.runtimeVersions())
         if (rebuild) {
            return true
         }

         // If no changes, tell the upstream job to use the previous build artifacts.
         def component = script.getComponentParts()['repo']
         def depDir = "${component}_DEP_DIR"
         script.env."$depDir" = lastBuildLocation
         script.echo "No changes since last successful build. Setting build output to $lastBuildLocation."
         return false
      }
   }

   private void runBuild() {
      def builders = [:]

      for(LabviewBuildVersion version : pipelineInformation.lvVersions) {

         // need to bind the variable before the closure - can't do 'for (version in lvVersions)'
         def lvVersion = version

         String nodeLabel = lvVersion.lvRuntimeVersion
         if (pipelineInformation.nodeLabel?.trim()) {
            nodeLabel = "$nodeLabel && ${pipelineInformation.nodeLabel}"
         }

         builders[lvVersion] = {
            script.node(nodeLabel) {
               setup(lvVersion)

               def configuration = BuildConfiguration.loadString(script, jsonConfig, lvVersion)
               configuration.printInformation(script)

               def builder = new Builder(script, configuration, lvVersion, MANIFEST_FILE, changedFiles)
               def stages = builder.buildPipeline()

               executeStages(stages)
            }
         }
      }

      script.parallel builders
   }

   private void setup(lvVersion) {
      def manifest = script.readJSON text: '{}'

      script.stage("Checkout_$lvVersion") {
         script.deleteDir()
         script.echo 'Attempting to get source from repo.'
         script.timeout(time: checkoutTimeoutInMinutes, unit: 'MINUTES'){
            manifest['scm'] = script.checkout(script.scm)
         }
      }
      script.stage("Setup_$lvVersion") {
         script.cloneBuildTools()
         script.setLabVIEWPath(lvVersion)

         // Write a manifest
         script.echo "Writing manifest to $MANIFEST_FILE"
         script.writeJSON file: MANIFEST_FILE, json: manifest, pretty: 3
      }
   }

   private void sendNotification() {
      def pipelineResult = PipelineStatus.getResult(script)
      if (pipelineResult == PipelineResult.SUCCESS || pipelineResult == PipelineResult.ABORTED) {
         // Don't spam notifications if the build is consistently successful
         // or if the build was manually aborted
         return
      }

      // Send notification if defined in build.toml
      def configuration = getArbitraryVersionConfiguration()
      if (!configuration.notificationInfo) {
         return
      }

      Stage notifyStage = new Notify(script, pipelineInformation.nodeLabel, configuration.notificationInfo, pipelineResult)
      notifyStage.execute()
   }

   // This method is here to catch builds with issue 50:
   // https://github.com/ni/niveristand-custom-device-build-tools/issues/50
   // If this issue is encountered, the build will still show success even
   // though an export for the desired version is not actually created.
   // We should fail the build instead of returning false success.
   private void validateBuild() {
      String nodeLabel = ''
      if (pipelineInformation.nodeLabel?.trim()) {
         nodeLabel = pipelineInformation.nodeLabel
      }

      script.node(nodeLabel) {
         script.stage("Validation") {
            script.echo("Validating build output.")
            def component = script.getComponentParts()['repo']
            def exportDir = script.env."${component}_DEP_DIR"
            pipelineInformation.lvVersions.each { version ->
               if(!script.fileExists("$exportDir\\${version.lvRuntimeVersion}")) {
                  script.failBuild("Failed to build version $version. See issue: https://github.com/ni/niveristand-custom-device-build-tools/issues/50")
               }
            }

            createFinishedFile(exportDir)
         }
      }
   }

   // Create a file indicating the build is finished.
   // If this file exists, the build was successful.
   // If this file does not exist, the build was either unsuccessful
   // or is still in progress -- in either case, the archive should
   // not be consumed.
   private void createFinishedFile(exportDir) {
      script.bat "type nul > \"$exportDir\\.finished\""
   }

   private String getArbitraryVersionConfiguration() {
      def arbitraryLvVersion = pipelineInformation.lvVersions[0]
      def configuration = BuildConfiguration.loadString(script, jsonConfig, arbitraryLvVersion)
      return configuration
   }
}
