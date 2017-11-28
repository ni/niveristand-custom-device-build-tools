package ni.vsbuild.v3.steps

import ni.vsbuild.v3.BuildConfiguration

abstract class LvBuildStep extends LvProjectStep {

   def outputLibraries
   def outputDir
   def dependencyTarget
   
   LvBuildStep(script, jsonStep, lvVersion) {
      super(script, jsonStep, lvVersion)
      this.outputLibraries = jsonStep.optJSONArray('output_libraries')
      this.outputDir = jsonStep.optString('output_dir')
      this.dependencyTarget = jsonStep.optString('dependency_target')
   }

   void executeStep(BuildConfiguration configuration) {
      copyDependencies(configuration)
      
      def resolvedProject = resolveProject(configuration)
      def path = resolvedProject.getString('path')
      executeBuildStep(path)
      
      if(!(outputDir && outputLibraries)) {
         return
      }
      
      stageLibraries(outputDir, configuration)
   }
   
   protected void copyDependencies(BuildConfiguration configuration) {
      if(!configuration.dependencies) {
         return
      }
      
      // Must create another map and use this for iterating through libraries
      // because jenkins/groovy don't like copying the file in the same loop
      // as accessing the JSON objects: java.util.Collections$UnmodifiableCollection$1
      def libraryDeps = [:]
      
      def dependencies = configuration.dependencies
      for(def key in dependencies.keys()) {
         def archiveDir = script.env."${key}_DEP_DIR"
         
         def dependency = dependencies.getJSONObject(key)
         def copyLocation = dependency.getString('copy_location')
         def libraries = dependency.getJSONArray('libraries')
         
         for(def library : libraries) {
            libraryDeps["$archiveDir\\$lvVersion\\$dependencyTarget\\$library"] = "$copyLocation\\$library"
         }
      }

      for(def key : libraryDeps.keySet()) {
         script.bat "copy /y \"$key\" \"${libraryDeps.get(key)}\""
      }
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
   
   protected abstract void executeBuildStep(String projectPath)
}
