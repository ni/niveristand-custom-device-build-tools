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
      def resolvedProject = resolveProject(configuration)
      executeBuildStep(resolvedProject)
         
      def outputDir = entry.optString('output_dir')
      if(!(outputDir || outputLibraries)) {
         return
      }
      
      moveLibraries(outputDir, configuration)
   }
   
   protected def resolveProject(BuildConfiguration configuration) {      
      def dereferencedProject = (project =~ /(\w)+/)[0][0]
      def projectRef = configuration.projects.getJSONObject(dereferencedProject)
      return projectRef
   }
   
   protected void moveLibraries(String outputDir, BuildConfiguration configuration) {      
      script.dir(configuration.archive.getString('build_output_dir')) {
         script.bat "mkdir \"$outputDir\""
         for(def library : outputLibraries) {
            script.bat "move \"$library\" \"$outputDir\\$library\""
         }
      }
   }
   
   protected abstract void executeBuildStep(projectEntry)
}
