package ni.vsbuild.steps

import ni.vsbuild.BuildConfiguration

class PlaceholderStep extends LvStep {

   def vi

   PlaceholderStep(script, mapStep, lvVersion) {
      super(script, mapStep, lvVersion)
      this.vi = mapStep.get('vi')
   }

   void executeStep(BuildConfiguration configuration) {
      script.lvRunVi(vi, lvVersion)
   }
}