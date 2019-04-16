package ni.vsbuild.steps

import ni.vsbuild.BuildConfiguration

class LvVITesterStep {

   def testPath

   LvVITesterStep(script, mapStep, lvVersion) {
      super(script, mapStep, lvVersion)
      this.testPath = mapStep.get('test_path')
   }

   void executeStep(BuildConfiguration configuration) {
      script.cloneVSTestTools()
      script.lvVITester(testPath, lvVersion)
   }
}
