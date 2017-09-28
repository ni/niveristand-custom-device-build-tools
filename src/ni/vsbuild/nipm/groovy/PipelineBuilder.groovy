package ni.vsbuild.nipm.groovy

class PipelineBuilder {

   private final def builder
   
   PipelineBuilder(builder) {
      this.builder = builder
   }
   
   void buildPipeline {
      builder.withInitialCleanStage()
      builder.withCheckoutStage()
      
      builder.withSetupStage()
      
      builder.withCodegenStage()
      builder.withBuildStage()
      
      builder.withArchiveStage()
      builder.withPackageStage()
      
      builder.withPublishStage()
      
      builder.WithCleanupStage()
   }
}
