package ni.vsbuild

import ni.vsbuild.stages.Stage

abstract class AbstractPipeline implements Pipeline {

   def script
   def prebuildStages = []
   def buildStages = []
   BuildInformation buildInformation
   
   protected AbstractPipeline(PipelineBuilder builder) {
      this.script = builder.script
      this.buildInformation = builder.buildInformation
      this.prebuildStages = builder.prebuildStages
      this.buildStages = builder.buildStages
   }
   
   abstract void execute()
   
   protected void executeStages(stages, executor) {
      for (Stage stage : stages) {
         try {
            stage.execute(executor)
         } catch (err) {
            script.currentBuild.result = "FAILURE"
            script.error "Build failed: ${err.getMessage()}"
         }
      }
   }
}
