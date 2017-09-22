package ni.vsbuild.stages

abstract class AbstractStage implements Stage {

   def stageName
   def script

   AbstractStage(script, String stageName) {
      this.script = script
      this.stageName = stageName
   }

   void execute(executor) {
      script.stage("${stageName}_${executor.lvVersion}") {
         executeStage(executor)
      }
   }
   
   protected abstract void executeStage(executor)

}
