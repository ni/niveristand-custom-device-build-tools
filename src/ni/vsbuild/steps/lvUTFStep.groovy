package ni.vsbuild.steps

import ni.vsbuild.BuildConfiguration

class LvUTFStep extends LvProjectStep {

   def lvVersion

   LvUTFStep(script, mapStep, lvVersion) {
      this.script = script
   }

   void executeStep(BuildConfiguration configuration) {
      script.echo "SRPSM: LvUTFStep.executeStep"
   }
}
