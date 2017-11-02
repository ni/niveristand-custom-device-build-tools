package ni.vsbuild.v2.stages

import ni.vsbuild.v2.steps.Step
import ni.vsbuild.v2.steps.StepFactory

class Build extends AbstractStage {

   Build(script, configuration) {
      super(script, 'Build', configuration)
   }
   
   void executeStage() {
      List<Step> steps = []
      script.echo "build is ${configuration.build}"
      def jsonSteps = configuration.build.getJSONArray('steps')
      for (def jsonStep in jsonSteps) {
         Step step = StepFactory.create(script, jsonStep)
         steps.add(step)
      }
      
      for(Step step in steps) {
         step.execute(configuration)
      }
   }
}
