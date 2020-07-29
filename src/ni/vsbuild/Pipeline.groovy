package ni.vsbuild

import ni.vsbuild.stages.*

class Pipeline implements Serializable {

   private static final String JSON_FILE = 'build.json'

   private static final String MANIFEST_FILE = 'Built/installer/manifest.json'

   def script
   PipelineInformation pipelineInformation
   String jsonConfig
   def changedFiles

   static class Builder implements Serializable {

      def script
      BuildConfiguration buildConfiguration
      String lvVersion
      String manifestFile
      def changedFiles
      def stages = []

      Builder(def script, BuildConfiguration buildConfiguration, String lvVersion, String manifestFile, def changedFiles) {
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
         // The plan is to enable automatic merging from master to
         // release or hotfix branch packages and not build packages
         // for any other branches, including master. The version must
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
         readBuildInformation()
         validateShouldBuildPipeline()

         // build dependencies before starting this pipeline
         script.buildDependencies(pipelineInformation)

         runBuild()
         validateBuild()
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

   private void readBuildInformation() {
      def manifest = script.readJSON text: '{}'
      def config
      script.node(pipelineInformation.nodeLabel) {
         script.stage('Checkout_readBuildInformation') {
            script.deleteDir()
            script.echo 'Attempting to get source from repo.'
            script.timeout(time: 10, unit: 'MINUTES'){
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
   }

   private void validateShouldBuildPipeline() {
      // We do not want to rebuild if our output would clobber existing data.
      // This can happen if the Jenkins build numbers reset, or e.g. due to
      // multiple repositories unintentionally exporting to the same location.
      def configuration = getArbitraryVersionConfiguration()
      if (!configuration.archive) {
         // We won't clobber anything if we aren't archiving
         return
      }

      def archiveParentLocation = Archive.calculateArchiveParentLocation(script, configuration)
      def archiveLocation = Archive.calculateArchiveLocation(script, configuration)
      script.node(pipelineInformation.nodeLabel) {
         script.stage('validateShouldBuildPipeline') {
            if (script.fileExists(archiveParentLocation + "\\norebuild")) {
               script.failBuild("Refusing to build, norebuild file exists.")
            }

            if (script.fileExists(archiveLocation)) {
               script.failBuild("Refusing to build, $archiveLocation already exists and would be overwritten.")
            }
         }
      }
   }

   private void runBuild() {
      def builders = [:]

      for(String version : pipelineInformation.lvVersions) {

         // need to bind the variable before the closure - can't do 'for (version in lvVersions)'
         def lvVersion = version

         String nodeLabel = lvVersion
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
         script.timeout(time: 5, unit: 'MINUTES'){
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
               if(!script.fileExists("$exportDir\\$version")) {
                  script.failBuild("Failed to build version $version. See issue: https://github.com/ni/niveristand-custom-device-build-tools/issues/50")
               }
            }
         }
      }
   }

   private String getArbitraryVersionConfiguration() {
      def arbitraryLvVersion = pipelineInformation.lvVersions[0]
      def configuration = BuildConfiguration.loadString(script, jsonConfig, arbitraryLvVersion)
      return configuration
   }
}
