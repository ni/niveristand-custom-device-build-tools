package ni.vsbuild.v3.steps

import ni.vsbuild.v3.BuildConfiguration

class LvRunViStep extends LvStep {

   def vi
   
   LvRunViStep(script, jsonStep, lvVersion) {
      super(script, jsonStep, lvVersion)
      this.vi = jsonStep.get('vi')
   }
   
   void executeStep(BuildConfiguration configuration) {
      script.lvRunVi(vi, lvVersion)
   }
}
