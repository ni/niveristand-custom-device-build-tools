package ni.vsbuild.stages

class Codegen extends AbstractStepStage {

   Codegen(script, configuration, lvVersion) {
      super(script, 'Codegen', configuration, lvVersion)
   }

   void executeStage() {
      if(configuration.projects) {
         generateProjectConfigFiles(lvVersion)
      }
      if(configuration.codegen) {
         executeSteps(configuration.codegen)
      }
   }

   // Generates a config file with the correct VeriStand assembly
   // versions so the API can correctly load. A config file is
   // generated for any project defined in the BuildConfiguration projects
   private void generateProjectConfigFiles(lvVersion) {      
      def projects = configuration.getProjectList()

      for(def project : projects) {
         def path = project.get('path')
         script.copyProjectConfig(path, lvVersion)
      }
   }
}
