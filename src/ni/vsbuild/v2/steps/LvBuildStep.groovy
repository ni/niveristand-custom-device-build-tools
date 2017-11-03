package ni.vsbuild.v2.steps

import ni.vsbuild.v2.BuildConfiguration

abstract class LvBuildStep extends LvStep {

   def project
   def outputLibraries
   
   LvBuildStep(script, jsonStep, lvVersion) {
      super(script, jsonStep, lvVersion)
      this.project = jsonStep.getString('project')
      this.outputLibraries = jsonStep.optJSONArray('output_libraries')
   }

   void executeStep(BuildConfiguration configuration) {
      def projects = resolveProjects(configuration)
      for(def entry : projects) {
         executeBuildStep(entry.getString('path'))
         
         def outputDir = entry.optString('output_dir')
         if(!(outputDir || outputLibraries)) {
            return
         }
         
         moveLibraries(outputDir, configuration)
      }
   }
   
   protected def resolveProjects(BuildConfiguration configuration) {
      def projects = []
      
      if(project == 'all') {
         return configuration.getProjectList()
      }
      
      def dereferencedProject = (project =~ /(\w)+/)[0][0]
      def projectRef = configuration.projects.getJSONObject(dereferencedProject)
      return [projectRef]
   }
   
   protected void moveLibraries(String outputDir, BuildConfiguration configuration) {      
      def sourceDir = configuration.archive.getString('build_output_dir')
      def destDir = "$sourceDir\\$outputDir"
      script.bat "mkdir \"$destDir\""
      for(def library : outputLibraries) {
         script.bat "move \"$sourceDir\\$library\" \"$destDir\\$library\""
      }
   }
   
   protected abstract void executeBuildStep(String projectPath)
}
