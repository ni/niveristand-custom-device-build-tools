package ni.vsbuild.v2.steps

import ni.vsbuild.v2.BuildConfiguration

abstract class LvBuildStep extends LvStep {

   def project
   def outputLibraries
   def outputDir
   def dependencyTarget
   
   LvBuildStep(script, jsonStep, lvVersion) {
      super(script, jsonStep, lvVersion)
      this.project = jsonStep.getString('project')
      this.outputLibraries = jsonStep.optJSONArray('output_libraries')
      this.outputDir = jsonStep.optString('output_dir')
      this.dependencyTarget = jsonStep.optString('dependency_target')
   }

   void executeStep(BuildConfiguration configuration) {
      copyDependencies(configuration)
      
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
   
   protected void copyDependencies(BuildConfiguration configuration) {
      if(!configuration.dependencies) {
         return
      }
      
      script.bat "set"
      def dependencies = configuration.dependencies
      for(def key in dependencies.keys()) {
         script.echo "key is $key"
         def dependency = dependencies.getJSONObject(key)
         script.echo "Dependency is $dependency"
         def envVar = "${key}_DEP_DIR"
         script.echo "envVar is $envVar"
         def archiveDir = script.env.envVar
         script.echo "archiveDir is $archiveDir"
         def copyLocation = dependency.getString('copy_location')
         script.echo "Dependency copy location is $copyLocation"
          def libraries = dependency.getJSONArray('libraries')
         for(def library : libraries) {
            script.echo "Library is $library"
            script.bat "copy /y \"$archiveDir\\$dependencyTarget\\$library\" \"$copyLocation\\$library\""
         }
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
   
   protected abstract void executeBuildStep(projectEntry)
}
