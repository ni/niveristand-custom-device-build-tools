package ni.vsbuild.v2.steps

import ni.vsbuild.v2.BuildConfiguration

abstract class LvProjectStep extends LvStep {

   def project
   
   LvBuildStep(script, jsonStep, lvVersion) {
      super(script, jsonStep, lvVersion)
      this.project = jsonStep.getString('project')
   }
   
   protected def resolveProject(BuildConfiguration configuration) {      
      def dereferencedProject = (project =~ /(\w)+/)[0][0]
      def projectRef = configuration.projects.getJSONObject(dereferencedProject)
      return projectRef
   }
}
