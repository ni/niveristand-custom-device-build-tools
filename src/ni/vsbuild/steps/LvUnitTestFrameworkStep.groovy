package ni.vsbuild.steps

import ni.vsbuild.BuildConfiguration

class LvUnitTestFrameworkStep extends LvStep {

   def vi

   LvUnitTestFrameworkStep(script, mapStep, lvVersion) {
      super(script, mapStep, lvVersion)
      this.vi = mapStep.get('vi')
   }

   void executeStep(BuildConfiguration configuration) {
      script.lvRunVi(vi, lvVersion)
   }
}
