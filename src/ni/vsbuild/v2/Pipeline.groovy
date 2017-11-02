package ni.vsbuild.v2

import ni.vsbuild.v2.stages.*

class Pipeline implements Serializable {

   def script
   def stages = []
   
   static class Builder implements Serializable {
      
      def script
      BuildConfiguration configuration
      def stages = []
      
      Builder(def script, BuildConfiguration configuration) {
         this.script = script
         this.configuration = configuration
      }
      
      def withCodegenStage() {
         stages << new Codegen(script, configuration)
      }
      
      def withBuildStage() {
         stages << new Build(script, configuration)
      }
      
      def withArchiveStage() {
         stages << new Archive(script, configuration)
      }
      
      def buildPipeline() {         
         if(configuration.codegen || configuration.projects) {
            withCodegenStage()
         }
         
         if(configuration.build) {
            withBuildStage()
         }
         
         if(configuration.archive) {
            withArchiveStage
         }
         
         return stages
      }
   }
   
   Pipeline(script) {
      this.script = script
   }
   
   void execute() {
      script.node('dcafbuild01') {
         setup()
         
         def configuration = BuildConfiguration.load(script, 'build.json')
         configuration.printInformation(script)
         
         def builder = new Builder(script, configuration)
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
