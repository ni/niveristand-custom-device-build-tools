package ni.vsbuild.steps

import ni.vsbuild.BuildConfiguration

class LvUnitTestFrameworkStep extends LvProjectStep {

   def UTF_path

   LvUnitTestFrameworkStep(script, mapStep, lvVersion) {
      super(script, mapStep, lvVersion)
      this.UTF_path = mapStep.get('UTF_path')
   }

   void executeStep(BuildConfiguration configuration) {
      script.lvUTF(UTF_path, lvVersion)
   }
}
