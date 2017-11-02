package ni.vsbuild.v2.steps

import ni.vsbuild.v2.BuildConfiguration

class LvRunViStep extends LvStep {

   def vi
   
   LvRunViStep(script, jsonStep) {
      super(script, jsonStep)
      this.vi = jsonStep.getString('vi')
   }
   
   void executeStep(BuildConfiguration configuration) {
      script.lvRunVi(vi, lvVersion)
   }
}
