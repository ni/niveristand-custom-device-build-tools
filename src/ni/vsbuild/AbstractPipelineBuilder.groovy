package ni.vsbuild

import ni.vsbuild.stages.*

abstract class AbstractPipelineBuilder implements PipelineBuilder {

   protected def script
   protected BuildInformation buildInformation
   
   def prebuildStages = []
   def buildStages = []
   def postbuildStages = []
   
   AbstractPipelineBuilder(script, BuildInformation buildInformation) {
      this.script = script
      this.buildInformation = buildInformation
   }
   
   public abstract void buildPipeline()
   
   def withInitialCleanStage(section = prebuildStages) {
      section << new InitialClean(script)
   }

   def withCheckoutStage(section = prebuildStages) {
      section << new Checkout(script)
   }
   
   def withSetupStage(section = buildStages) {
      section << new Setup(script)
   }
   
   def withUnitTestStage(section = postbuildStages) {
      section << new UnitTest(script)
   }
   
   def withCodegenStage(section = buildStages) {
      section << new Codegen(script)
   }
   
   def withBuildStage(section = buildStages) {
      section << new Build(script)
   }
   
   def withArchiveStage(section = buildStages) {
      section << new Archive(script)
   }
   
   def withPackageStage(section = buildStages) {
      section << new PackageBuild(script)
   }
   
   def withPublishStage(section = postbuildStages) {
      section << new Publish(script)
   }
   
   def withCleanupStage(section = postbuildStages) {
      section << new Cleanup(script)
   }
}
