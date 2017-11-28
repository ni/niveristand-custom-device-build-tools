package ni.vsbuild.v3.steps

import ni.vsbuild.v3.BuildConfiguration

abstract class LvProjectStep extends LvStep {

   def project
   
   LvProjectStep(script, jsonStep, lvVersion) {
      super(script, jsonStep, lvVersion)
      this.project = jsonStep.get('project')
   }
   
   protected def resolveProject(BuildConfiguration configuration) {      
      def dereferencedProject = (project =~ /(\w)+/)[0][0]
      def projectRef = configuration.projects.get(dereferencedProject)
      return projectRef
   }
}
