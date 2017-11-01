package ni.vsbuild.v2.steps.build

import ni.vsbuild.v2.BuildConfiguration

class LvBuildSpec extends LvBuildStep {

   def target
   def spec
   
   LvBuildSpec(script, jsonStep) {
      super(script, jsonStep)
      this.target = jsonStep.getString('target')
      this.spec = jsonStep.getString('build_spec')
   }
   
   void execute(BuildConfiguration configuration) {
      script.echo "This is the execute for LvBuildSpec class."
   }

}