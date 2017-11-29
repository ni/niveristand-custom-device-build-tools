package ni.vsbuild.v3.stages

import ni.vsbuild.v3.steps.Step
import ni.vsbuild.v3.steps.StepFactory

abstract class AbstractStepStage extends AbstractStage {

   AbstractStepStage(script, stageName, configuration, lvVersion) {
      super(script, stageName, configuration, lvVersion)
   }

   protected void executeSteps(String stepList) {
      List<Step> steps = StepFactory.create(script, configuration, stepList, lvVersion)
      for(Step step : steps) {
         step.execute(configuration)
      }
   }
}
