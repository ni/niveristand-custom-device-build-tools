package ni.vsbuild

abstract class AbstractPipelineBuilder implements PipelineBuilder, Serializable {

   protected def script
   protected BuildInformation buildInformation
   
   def prebuildStages = []
   def buildStages = []
   
   AbstractPipelineBuilder(script, BuildInformation buildInformation) {
      this.script = script
      this.buildInformation = buildInformation
   }
   
   public abstract void buildPipeline()
   
   def withInitialCleanStage() {
      prebuildStages << new InitialClean(script)
   }

   def withCheckoutStage() {
      prebuildStages << new Checkout(script)
   }
   
   def withSetupStage() {
      buildStages << new Setup(script)
   }
   
   def withUnitTestStage() {
      buildStages << new UnitTest(script)
   }
   
   def withCodegenStage() {
      buildStages << new Codegen(script)
   }
   
   def withBuildStage() {
      buildStages << new Build(script)
   }
   
   def withArchiveStage() {
      buildStages << new Archive(script)
   }
   
   def withPackageStage() {
      buildStages << new PackageBuild(script)
   }
   
   def withPublishStage() {
      buildStages << new Publish(script)
   }
   
   def withCleanupStage() {
      buildStages << new Cleanup(script)
   }
}
