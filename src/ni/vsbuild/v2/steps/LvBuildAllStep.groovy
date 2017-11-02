package ni.vsbuild.v2.steps

import ni.vsbuild.v2.BuildConfiguration

class LvBuildAllStep extends LvBuildStep {
   
   LvBuildAllStep(script, jsonStep) {
      super(script, jsonStep)
   }
   
   void execute(BuildConfiguration configuration) {
      script.lvBuildAll(resolveProject(configuration), '2017')
   }
}
