package ni.vsbuild.nipm.nibuild

import ni.vsbuild.AbstractPipeline
import ni.vsbuild.AbstractPipelineBuilder
import ni.vsbuild.BuildInformation

class Pipeline extends AbstractPipeline {

   static builder(script, BuildInformation buildInformation) {
      return new Builder(script, buildInformation)
   }
   
   static class Builder extends AbstractPipelineBuilder {
      
      Builder(def script, BuildInformation buildInformation) {
         super(script, buildInformation)
      }
      
      public void build() {
         withInitialCleanStage()
         withCheckoutStage()
         
         withBuildStage()
         withUnitTestStage()
         withPackageStage()
         
         withCleanupStage()
         
         return new Pipeline(this)
      }
   }
   
   private Pipeline(builder) {
      super(builder)
   }
   
   void execute() {      
      buildInformation.printInformation(script)
      
      // this is not used for nibuild execution, but is required for creating the executor
      def lvVersion
      
      script.node(buildInformation.nodeLabel) {
         
         def executor
         
         executeStages(prebuildStages, executor)
         
         // This load must happen after the checkout stage, but before any
         // stage that requires the build steps to be loaded
         executor = buildInformation.createExecutor(script, lvVersion)
         
         executeStages(buildStages, executor)
      }
   }
}
