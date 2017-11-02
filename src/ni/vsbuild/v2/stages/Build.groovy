package ni.vsbuild.v2.stages

import ni.vsbuild.v2.steps.Step
import ni.vsbuild.v2.steps.StepFactory

class Build extends AbstractStage {

   Build(script, configuration) {
      super(script, 'Build', configuration)
   }
   
   void executeStage() {
      List<Step> steps = StepFactory.create(script, configuration, 'build')
      for(Step step in steps) {
         step.execute(configuration)
      }
   }
}
