package ni.vsbuild.steps

import ni.vsbuild.BuildConfiguration

class LvRunViStep extends LvStep {

   LvRunViStep(script, mapStep, lvVersion) {
      super(script, mapStep, lvVersion)
   }

   void executeStep(BuildConfiguration configuration) {
      script.lvClearCache(lvVersion)
   }
}
