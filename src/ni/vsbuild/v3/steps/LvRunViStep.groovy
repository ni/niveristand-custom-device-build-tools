package ni.vsbuild.v3.steps

import ni.vsbuild.v3.BuildConfiguration

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
