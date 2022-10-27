package ni.vsbuild.steps

import ni.vsbuild.BuildConfiguration

class LvClearCacheStep extends LvStep {

   LvClearCacheStepStep(script, mapStep, lvVersion) {
      super(script, mapStep, lvVersion)
   }

   void executeStep(BuildConfiguration configuration) {
      script.lvClearCache(lvVersion)
   }
}
