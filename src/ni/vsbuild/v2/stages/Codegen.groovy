package ni.vsbuild.v2.stages

import ni.vsbuild.v2.steps.Step
import ni.vsbuild.v2.steps.StepFactory

class Codegen extends AbstractStepStage {

   Codegen(script, configuration) {
      super(script, 'Codegen', configuration)
   }
   
   void executeStage() {
      def paths = generateProjectConfigFiles()
      script.copyProjectConfig(paths[0], '2017')
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
      return paths
   }
}
