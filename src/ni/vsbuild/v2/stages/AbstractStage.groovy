package ni.vsbuild.v2.stages

import ni.vsbuild.v2.PipelineConfiguration

abstract class AbstractStage implements Stage {

   def stageName
   def script
   def configuration

   AbstractStage(script, String stageName, PipelineConfiguration configuration) {
      this.script = script
      this.stageName = stageName
      this.configuration = configuration
   }

   void execute() {
      script.stage("$stageName") {
         executeStage()
      }
   }
   
   abstract void executeStage()

}
