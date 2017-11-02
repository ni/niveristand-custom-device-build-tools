package ni.vsbuild.v2.steps

import ni.vsbuild.v2.BuildConfiguration

class LvBuildAllStep extends LvBuildStep {
   
   LvBuildAllStep(script, jsonStep, lvVersion) {
      super(script, jsonStep, lvVersion)
   }
   
   void executeStep(BuildConfiguration configuration) {
      script.lvBuildAll(resolveProject(configuration), lvVersion)
   }
}
