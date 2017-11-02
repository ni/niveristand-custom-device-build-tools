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
      
      def paths = []
      for(def key in configuration.projects.keys()) {
         def project = configuration.projects.getJSONObject(key)
         def path = project.getString('path')
         paths.add(path)
      }
      
      for(def path in paths) {
         script.copyProjectConfig(path, '2017')
      }
   }
}
