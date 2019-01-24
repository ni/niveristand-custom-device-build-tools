package ni.vsbuild.steps

import ni.vsbuild.BuildConfiguration

class LvUnitTestFrameworkStep extends LvStep {

   def vi

   LvUnitTestFrameworkStep(script, mapStep, lvVersion) {
      super(script, mapStep, lvVersion)
   }

   void executeStep(BuildConfiguration configuration) {
      script.lvUTF(vi, lvVersion)
   }
}
