package ni.vsbuild.steps

import ni.vsbuild.BuildConfiguration
import ni.vsbuild.StringSubstitution

abstract class LvBuildStep extends LvProjectStep {

   def outputLibraries
   def outputDir
   def dependencyTarget

   LvBuildStep(script, mapStep, lvVersion) {
      super(script, mapStep, lvVersion)
      this.outputLibraries = mapStep.get('output_libraries')
      this.outputDir = mapStep.get('output_dir')
      this.dependencyTarget = mapStep.get('dependency_target')
   }

   void executeStep(BuildConfiguration configuration) {
      copyDependencies(configuration)

      def resolvedProject = resolveProject(configuration)
      def path = resolvedProject.get('path')
      executeBuildStep(path)

      if(!(outputDir && outputLibraries)) {
         return
      }

      stageLibraries(outputDir, configuration)
   }

   protected void copyDependencies(BuildConfiguration configuration) {
      if(!(configuration.dependencies && dependencyTarget)) {
         return
      }

      def dependencies = configuration.dependencies
      for(def key : dependencies.keySet()) {
         def dependencyDir = getDependencyPath(key)
         def dependency = dependencies.get(key)
         def copyLocation = dependency.get('copy_location')
         def libraries = dependency.get('libraries')

         for(def library : libraries) {
            def libraryName = getLibraryName(library)
            def substitutedLibraryPath = StringSubstitution.replaceStrings(library, lvVersion, ['target' : dependencyTarget])
            script.bat "copy /y \"$dependencyDir\\$substitutedLibraryPath\" \"$copyLocation\\$libraryName\""
         }
      }
   }

   protected void stageLibraries(String outputDir, BuildConfiguration configuration) {      
      def buildOutputDir = configuration.archive.get('build_output_dir')
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

   // The following methods are protected instead of private due to
   // an issue with closures in child classes. Making these private
   // causes a MissingMethodException for these methods because Groovy
   // is looking for the definition in the child class.
   // https://dzone.com/articles/groovy-closures-do-not-have
   protected String getDependencyPath(String key) {
      def dependencyDir = script.env."${key}_DEP_DIR"
      if(dependencyDir) {
         return "$dependencyDir\\$lvVersion\\$dependencyTarget"
      }

      // Dependency was not built as part of dependencies.
      // Use local dependency directory
      return script.env.WORKSPACE
   }

   protected String getLibraryName(String library) {
      return library.tokenize("\\").last()
   }

}
