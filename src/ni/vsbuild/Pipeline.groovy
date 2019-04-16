package ni.vsbuild

import ni.vsbuild.stages.*

class Pipeline implements Serializable {

   private static final String JSON_FILE = 'build.json'

   private static final String MANIFEST_FILE = 'Built/installer/manifest.json'

   def script
   PipelineInformation pipelineInformation
   def stages = []

   static class Builder implements Serializable {

      def script
      BuildConfiguration buildConfiguration
      String lvVersion
      def stages = []

      Builder(def script, BuildConfiguration buildConfiguration, String lvVersion) {
         this.script = script
         this.buildConfiguration = buildConfiguration
         this.lvVersion = lvVersion
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
         stages << new Package(script, buildConfiguration, lvVersion)
      }

      def withArchiveStage() {
         stages << new Archive(script, buildConfiguration, lvVersion)
      }

      // The plan is to enable automatic merging from master to
      // release or hotfix branch packages and not build packages
      // for any other branches, including master. The version must
      // be appended to the release or hotfix branch name after a
      // dash (-) or slash (/).
      def shouldBuildPackage() {
         return buildConfiguration.packageInfo &&
            (script.env.BRANCH_NAME.startsWith("release") ||
            script.env.BRANCH_NAME.startsWith("hotfix"))
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

         if(shouldBuildPackage()) {
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

      // build dependencies before starting this pipeline
      script.buildDependencies(pipelineInformation)

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

               def configuration = BuildConfiguration.load(script, JSON_FILE)
               configuration.printInformation(script)

               def builder = new Builder(script, configuration, lvVersion)
               this.stages = builder.buildPipeline()

               executeStages()
            }
         }
      }

      script.parallel builders
   }

   protected void executeStages() {
      for (Stage stage : stages) {
         try {
            stage.execute()
         } catch (err) {
            script.failBuild(err.getMessage())
         }
      }
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
         script.buildSetup(lvVersion)

         // Write a manifest
         script.echo "Writing manifest to $MANIFEST_FILE"
         script.writeJSON file: MANIFEST_FILE, json: manifest, pretty: 3
      }
   }
}
