package ni.vsbuild.nipm

import ni.vsbuild.BuildInformation
import ni.vsbuild.shared.stages.*

class Pipeline implements Serializable {

   def script
   def stages = []
   BuildInformation buildInformation

   static builder(script, BuildInformation buildInformation) {
      return new Builder(script, buildInformation)
   }

   static class Builder implements Serializable {

      def script
      def stages = []
      BuildInformation buildInformation
      def executor

      Builder(def script, BuildInformation buildInformation) {
         this.script = script
         this.buildInformation = buildInformation
         executor = buildInformation.createExecutor()
      }

      def withInitialCleanStage() {
         stages << new InitialClean(script)
      }

      def withCheckoutStage() {
         stages << new Checkout(script)
      }
    
      def withSetupStage() {
         stages << new Setup(script, executor)
      }
      
      def withUnitTestStage() {
         stages << new UnitTest(script, executor)
      }
      
      def withCodegenStage() {
         stages << new Codegen(script, executor)
      }
      
      def withBuildStage() {
         stages << new Build(script, executor)
      }
      
      def withArchiveStage() {
         stages << new Archive(script, executor)
      }
      
      def withPackageStage() {
         stages << new PackageBuild(script, executor)
      }
      
      def withPublishStage() {
         stages << new Publish(script, executor)
      }
      
      def withCleanupStage() {
         stages << new Cleanup(script)
      }
      
      def buildPipeline() {
         withInitialCleanStage()
         withCheckoutStage()
         withSetupStage()
         
         if(buildInformation.officiallySupported) {
            withUnitTestStage()
         }
         
         withCodegenStage()
         withBuildStage()
         
         if(!buildInformation.officiallySupported) {
            withArchiveStage()
         }
         
         withPackageStage()
         
         if(script.env['BRANCH_NAME'] == 'master') {
            withPublishStage()
         }
         
         withCleanupStage()
         
         return new Pipeline(this)
      }
   }

   private Pipeline(Builder builder) {
      this.script = builder.script
      this.stages = builder.stages
      this.buildInformation = builder.buildInformation
   }

   void execute() {

      script.node(buildInformation.nodeLabel) {
         for (Stage stage : stages) {
            try {
               stage.execute()
            } catch (err) {
               script.currentBuild.result = "FAILURE"
               script.error "Build failed: ${err.getMessage()}"
            }
         }
      }
   }
}
