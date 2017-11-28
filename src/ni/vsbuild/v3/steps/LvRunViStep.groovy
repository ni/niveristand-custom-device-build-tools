package ni.vsbuild.v2.steps

import ni.vsbuild.v2.BuildConfiguration

class LvRunViStep extends LvStep {

   def vi
   
   LvRunViStep(script, jsonStep, lvVersion) {
      super(script, jsonStep, lvVersion)
      this.vi = jsonStep.getString('vi')
   }
   
   void executeStep(BuildConfiguration configuration) {
      script.lvRunVi(vi, lvVersion)
   }
}
