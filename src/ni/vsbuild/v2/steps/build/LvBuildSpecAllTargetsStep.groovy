package ni.vsbuild.v2.steps.build

import ni.vsbuild.v2.BuildConfiguration

class LvBuildSpecAllTargetsStep extends LvBuildStep {

   def spec
   
   LvBuildSpecAllTargetsStep(script, jsonStep) {
      super(script, jsonStep)
      this.spec = jsonStep.getString('build_spec')
   }
   
   void execute(BuildConfiguration configuration) {
      script.lvBuildSpecAllTargets(resolveProject(configuration), spec, '2017')
   }
}
