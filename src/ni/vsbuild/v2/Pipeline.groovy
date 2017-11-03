package ni.vsbuild.v2

import ni.vsbuild.v2.stages.*

class Pipeline implements Serializable {

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
      
      def withArchiveStage() {
         stages << new Archive(script, buildConfiguration, lvVersion)
      }
      
      def buildPipeline() {         
         if(buildConfiguration.codegen || buildConfiguration.projects) {
            withCodegenStage()
         }
         
         if(buildConfiguration.build) {
            withBuildStage()
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
      script.node("${pipelineInformation.nodeLabel}") {
         def lvVersion = pipelineInformation.lvVersions[0]
         setup()
         
         def configuration = BuildConfiguration.load(script, 'build.json')
         configuration.printInformation(script)
         
         def builder = new Builder(script, configuration, lvVersion)
         this.stages = builder.buildPipeline()
         
         executeStages()
      }
   }
   
   protected void executeStages() {
      for (Stage stage : stages) {
         try {
            stage.execute()
         } catch (err) {
            script.currentBuild.result = "FAILURE"
            script.error "Build failed: ${err.getMessage()}"
         }
      }
   }
   
   private void setup() {
      script.stage('Checkout') {
         script.deleteDir()
         script.echo 'Attempting to get source from repo.'
         script.timeout(time: 5, unit: 'MINUTES'){
            script.checkout(script.scm)
         }
      }
      script.stage('Setup') {
         script.cloneCommonbuild()
         script.bat "commonbuild\\scripts\\buildSetup.bat"
      }
   }
}
