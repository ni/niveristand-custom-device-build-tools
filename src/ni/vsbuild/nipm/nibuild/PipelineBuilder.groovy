package ni.vsbuild.nipm.nibuild

import ni.vsbuild.AbstractPipelineBuilder

class PipelineBuilder extends AbstractPipelineBuilder {

   PipelineBuilder(script, BuildInformation buildInformation) {
      super(script, buildInformation)
   }
   
   void build {
      withInitialCleanStage()
      withCheckoutStage()
      
      withSetupStage()
      
      withCodegenStage()
      withBuildStage()
      
      withArchiveStage()
      withPackageStage()
      
      withPublishStage()
      
      withCleanupStage()
   }
}
