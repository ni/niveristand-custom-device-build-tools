package ni.vsbuild.v2.steps.build

import ni.vsbuild.v2.BuildConfiguration

class LvBuildSpecStep extends LvBuildStep {

   def target
   def spec
   
   LvBuildSpecStep(script, jsonStep) {
      super(script, jsonStep)
      this.target = jsonStep.getString('target')
      this.spec = jsonStep.getString('build_spec')
   }
   
   void execute(BuildConfiguration configuration) {
      script.lvBuildSpec(resolveProject(configuration), target, spec, '2017')
   }
}
