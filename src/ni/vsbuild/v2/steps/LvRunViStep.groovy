package ni.vsbuild.v2.steps

import ni.vsbuild.v2.BuildConfiguration

class LvRunViStep extends AbstractStep {

   def vi
   
   LvRunViStep(script, jsonStep) {
      super(script, jsonStep)
      this.vi = jsonStep.getString('vi')
   }
   
   void executeStep(BuildConfiguration configuration) {
      script.lvRunVi(vi, '2017')
   }
}
