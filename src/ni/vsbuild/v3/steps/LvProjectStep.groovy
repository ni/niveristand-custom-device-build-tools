package ni.vsbuild.v3.steps

import ni.vsbuild.v3.BuildConfiguration

abstract class LvProjectStep extends LvStep {

   def project

   LvProjectStep(script, mapStep, lvVersion) {
      super(script, mapStep, lvVersion)
      this.project = mapStep.get('project')
   }

   protected def resolveProject(BuildConfiguration configuration) {      
      def dereferencedProject = (project =~ /(\w)+/)[0][0]
      def projectRef = configuration.projects.get(dereferencedProject)
      return projectRef
   }
}
