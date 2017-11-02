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
      if(project =~ /\{[\w]+\}/) {
         script.echo "project should be dereferenced"
      }
      def cleanedProject = project.replace("{", "").replace("}", "")
      def projectRef = configuration.projects.getJSONObject(cleanedProject)
      
      if(!projectRef) {
         return project
      }
      
      def path = projectRef.getString('path')
      return path
   }

}