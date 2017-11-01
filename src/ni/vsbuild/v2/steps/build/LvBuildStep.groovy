package ni.vsbuild.v2.steps.build

import ni.vsbuild.v2.steps.AbstractStep
import ni.vsbuild.v2.BuildConfiguration

abstract class LvBuildStep extends AbstractStep {

   def project
   
   LvBuildStep(script, jsonStep) {
      super(script, jsonStep)
      this.project = jsonStep.getString('project')
   }
   
   protected String resolveProject(BuildConfiguration configuration) {
      def cleanedProject = project.replace("\\{", "").replace("\\}")
      script.echo "Cleaned project is $cleanedProject"
      def projectRef = configuration.projects.getString(cleanedProject)
      script.echo "projectRef is $projectRef"
      
      if(!projectRef) {
         return project
      }
      
      def path = projectRef.getString('path')
      return path
   }

}