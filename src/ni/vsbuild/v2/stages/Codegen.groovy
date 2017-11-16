package ni.vsbuild.v2.stages

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
      def projects = configuration.getProjectList()
      def paths = []
      
      for(def project in projects) {
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
