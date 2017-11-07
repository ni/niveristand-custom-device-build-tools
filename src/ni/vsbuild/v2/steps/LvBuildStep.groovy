package ni.vsbuild.v2.steps

import ni.vsbuild.v2.BuildConfiguration

abstract class LvBuildStep extends LvStep {

   def project
   def outputLibraries
   def outputDir
   
   LvBuildStep(script, jsonStep, lvVersion) {
      super(script, jsonStep, lvVersion)
      this.project = jsonStep.getString('project')
      this.outputLibraries = jsonStep.optJSONArray('output_libraries')
      this.outputDir = jsonStep.optString('output_dir')
   }

   void executeStep(BuildConfiguration configuration) {
      def resolvedProject = resolveProject(configuration)
      executeBuildStep(resolvedProject)
      
      if(!(outputDir && outputLibraries)) {
         return
      }
      
      stageLibraries(outputDir, configuration)
   }
   
   protected def resolveProject(BuildConfiguration configuration) {      
      def dereferencedProject = (project =~ /(\w)+/)[0][0]
      def projectRef = configuration.projects.getJSONObject(dereferencedProject)
      return projectRef
   }
   
   protected void stageLibraries(String outputDir, BuildConfiguration configuration) {      
      def buildOutputDir = configuration.archive.getString('build_output_dir')
      def stageDir = BuildConfiguration.STAGING_DIR
      
      if(!script.fileExists("$stageDir")) {
         script.bat "mkdir \"$stageDir\""
      }
      
      script.bat "mkdir \"$stageDir\\$outputDir\""
      for(def library : outputLibraries) {
         script.bat "move \"$buildOutputDir\\$library\" \"$stageDir\\$outputDir\\$library\""
      }
   }
   
   protected abstract void executeBuildStep(projectEntry)
}
