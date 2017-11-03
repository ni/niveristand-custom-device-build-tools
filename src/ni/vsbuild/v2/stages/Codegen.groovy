package ni.vsbuild.v2.stages

import ni.vsbuild.v2.steps.Step
import ni.vsbuild.v2.steps.StepFactory

class Codegen extends AbstractStepStage {

   Codegen(script, configuration, lvVersion) {
      super(script, 'Codegen', configuration, lvVersion)
   }
   
   void executeStage() {
      if(configuration.projects) {
         generateProjectConfigFiles(lvVersion)
      }
      if(configuration.codegen) {
         executeSteps('codegen')
      }
   }
   
   // Generates a config file with the correct VeriStand assembly
   // versions so the API can correctly load. A config file is
   // generated for any project defined in the BuildConfiguration projects
   private void generateProjectConfigFiles(lvVersion) {      
      def paths = configuration.getAllProjectPaths()

      for(def path in paths) {
         script.copyProjectConfig(path, lvVersion)
      }
   }
}
