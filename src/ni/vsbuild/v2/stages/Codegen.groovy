package ni.vsbuild.v2.stages

import ni.vsbuild.v2.steps.Step
import ni.vsbuild.v2.steps.StepFactory

class Codegen extends AbstractStepStage {

   Codegen(script, configuration) {
      super(script, 'Codegen', configuration)
   }
   
   void executeStage() {
      generateProjectConfigFiles('2017')
      executeSteps('codegen')
   }
   
   // Generates a config file with the correct VeriStand assembly
   // versions so the API can correctly load. A config file is
   // generated for any project defined in the BuildConfiguration projects
   private void generateProjectConfigFiles(lvVersion) {
      if(!configuration.projects) {
         return
      }
      
      def paths = []
      for(def key in configuration.projects.keys()) {
         def project = configuration.projects.getJSONObject(key)
         def path = project.getString('path')
         paths.add(path)
      }
      
      // Must loop again because jenkins/groovy don't like reading the file
      // in the same loop as accessing the json objects:
      // java.util.Collections$UnmodifiableCollection$1
      for(def path in paths) {
         script.copyProjectConfig(path, lvVersion)
      }
   }
}
