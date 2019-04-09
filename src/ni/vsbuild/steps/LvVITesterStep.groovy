package ni.vsbuild.steps

import ni.vsbuild.BuildConfiguration

class LvVITesterStep extends LvProjectStep {

   def Test_path

   LvVITesterStep(script, mapStep, lvVersion) {
      super(script, mapStep, lvVersion)
      this.Test_path = mapStep.get('Test_path')
   }

   void executeStep(BuildConfiguration configuration) {
      script.cloneVSTestTools()
      script.lvVITester(Test_path, lvVersion)
   }
}
