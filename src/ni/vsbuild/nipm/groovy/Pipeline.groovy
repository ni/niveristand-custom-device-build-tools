package ni.vsbuild.nipm.groovy

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
      
      public void buildPipeline() {
         withInitialCleanStage()
         withCheckoutStage()
      
         withSetupStage()
      
         withCodegenStage()
         withBuildStage()
      
         withArchiveStage()
         withPackageStage()
      
         withPublishStage()
      
         withCleanupStage()
         
         return new Pipeline(this)
      }
   }
   
   private Pipeline(builder) {
      super(builder)
   }
   
   void execute() {
      def builders = [:]
      
      for(String version: buildInformation.lvVersions) {
         def lvVersion = version // need to bind the variable before the closure - can't do 'for (version in lvVersions)'
         builders[lvVersion] = {
            // build dependencies before starting this pipeline
            script.buildDependencies(buildInformation)
            
            script.node(getNodeLabel(lvVersion)) {
               
               def executor = buildInformation.createExecutor(script, lvVersion)
               
               //executeStages(prebuildStages, executor)
               
               // This load must happen after the checkout stage, but before any
               // stage that requires the build steps to be loaded
               //executor.loadBuildSteps(buildInformation.buildStepsLocation)
               
               executeStages(buildStages, executor)
            }
         }
      }
      
      script.parallel builders
   }
}
