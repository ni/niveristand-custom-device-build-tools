package ni.vsbuild.v2.steps.build

import ni.vsbuild.v2.steps.AbstractStep

abstract class LvBuildStep extends AbstractStep {

   def project
   
   LvBuildStep(script, jsonStep) {
      super(script, jsonStep)
      this.project = jsonStep.getString('project')
   }

}