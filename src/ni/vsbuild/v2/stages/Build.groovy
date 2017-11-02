package ni.vsbuild.v2.stages

import ni.vsbuild.v2.steps.Step
import ni.vsbuild.v2.steps.StepFactory

class Build extends AbstractStepStage {

   Build(script, configuration) {
      super(script, 'Build', configuration)
   }
   
   void executeStage() {
      executeSteps('build')
   }
}
