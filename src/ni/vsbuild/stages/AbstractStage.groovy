package ni.vsbuild.stages

import ni.vsbuild.BuildConfiguration
import ni.vsbuild.LvBuildVersion

abstract class AbstractStage implements Stage {

   def stageName
   def script
   def configuration
   def lvVersion

   AbstractStage(script, String stageName, BuildConfiguration configuration, LvBuildVersion lvVersion) {
      this.script = script
      this.stageName = stageName
      this.configuration = configuration
      this.lvVersion = lvVersion
   }

   void execute() {
      script.stage("${stageName}_${lvVersion}") {
         executeStage()
      }
   }

   abstract void executeStage()

}
