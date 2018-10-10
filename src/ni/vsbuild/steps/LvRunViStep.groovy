package ni.vsbuild.steps

import ni.vsbuild.BuildConfiguration

class LvRunViStep extends LvStep {

   def vi

   LvRunViStep(script, mapStep, lvVersion) {
      super(script, mapStep, lvVersion)
      this.vi = mapStep.get('vi')
   }

   void executeStep(BuildConfiguration configuration) {
      script.lvRunVi(vi, lvVersion)
   }
}
