package ni.vsbuild.steps

class LvUTFStep extends LvProjectStep {

   LvUTFStep(script, mapStep, lvVersion) {
      super(script, mapStep, lvVersion)
   }

   void executeStep(BuildConfiguration configuration) {
      script.lvRunVi(vi, lvVersion)
   }
}
