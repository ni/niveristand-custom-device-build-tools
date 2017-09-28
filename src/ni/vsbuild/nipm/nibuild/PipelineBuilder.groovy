package ni.vsbuild.nipm.nibuild

import ni.vsbuild.AbstractPipelineBuilder
import ni.vsbuild.BuildInformation

class PipelineBuilder extends AbstractPipelineBuilder {

   PipelineBuilder(script, BuildInformation buildInformation) {
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
   }
}
