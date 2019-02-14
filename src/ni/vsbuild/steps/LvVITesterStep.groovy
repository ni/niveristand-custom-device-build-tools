package ni.vsbuild.steps

import ni.vsbuild.BuildConfiguration

class LvVITesterStep extends LvProjectStep {

   def Test_path

   LvUnitTestFrameworkStep(script, mapStep, lvVersion) {
      super(script, mapStep, lvVersion)
      this.Test_path = mapStep.get('Test_path')
   }

   void executeStep(BuildConfiguration configuration) {
      script.lvVITester(Test_path, lvVersion)
   }
}
