package ni.vsbuild.v2.stages

import ni.vsbuild.v2.steps.Step
import ni.vsbuild.v2.steps.StepFactory

abstract class AbstractStepStage extends AbstractStage {
   
   AbstractStepStage(script, stageName, configuration) {
      super(script, stageName, configuration)
   }
   
   protected void executeSteps(String stepList) {
      List<Step> steps = StepFactory.create(script, configuration, stepList)
      for(Step step in steps) {
         step.execute(configuration)
      }
   }
}
