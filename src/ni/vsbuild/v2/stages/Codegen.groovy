package ni.vsbuild.v2.stages

import ni.vsbuild.v2.steps.Step
import ni.vsbuild.v2.steps.StepFactory

class Codegen extends AbstractStepStage {

   Codegen(script, configuration) {
      super(script, 'Codegen', configuration)
   }
   
   void executeStage() {
      generateProjectConfigFiles()
      executeSteps('codegen')
   }
   
   private void generateProjectConfigFiles() {
      if(!configuration.projects) {
         return
      }
      
      for(def key in configuration.projects.keys()) {
         script.echo "project type is ${project.getClass()}"
         def project = projects.getJSONObject(key)
         def path = project.getString('path')
         script.echo "Project path is $path"
      }
   }
}
