package ni.vsbuild.v2.steps

import ni.vsbuild.v2.BuildConfiguration

class LvBuildSpecStep extends LvBuildStep {

   def target
   def spec
   
   LvBuildSpecStep(script, jsonStep) {
      super(script, jsonStep)
      this.target = jsonStep.getString('target')
      this.spec = jsonStep.getString('build_spec')
   }
   
   void executeStep(BuildConfiguration configuration) {
      script.lvBuildSpec(resolveProject(configuration), target, spec, '2017')
   }
}
