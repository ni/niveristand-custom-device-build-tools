package ni.vsbuild.v2.steps.build

import ni.vsbuild.v2.steps.AbstractBuild

abstract class LvBuildStep extends AbstractBuild {

   def project
   
   LvBuildStep(script, jsonStep) {
      super(script, jsonStep)
      this.project = jsonStep.getString('project')
   }

}