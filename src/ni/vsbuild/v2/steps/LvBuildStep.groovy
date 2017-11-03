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
   
//   void executeStep(BuildConfiguration configuration) {
//      def projects = resolveProjectsMap(configuration)
//      for(def key : projects.keySet()) {
//         script.echo "key is $key"
//         def path = projects.get(key)
//         executeBuildStep(path)
         
//         if(!outputLibraries) {
//            return
//        }
//      }
//   }

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
      script.bat "mkdir $outputDir"
      
      def exportDir = "${BuildConfiguration.EXPORT_DIR}\\$outputDir"
      def moveSource = configuration.archive.getString('build_output_dir')
      for(def library : outputLibraries) {
         script.bat "move \"$moveSource\\$library\" \"$exportDir\\$library\""
      }
   }
   
   protected def resolveProjectsMap(BuildConfiguration configuration) {
      def projects = [:]
      
      if(project == 'all') {
         for(def projectEntry : configuration.getProjectList()) {
            def path = projectEntry.getString('path')
            projects["$projectEntry"] = path
         }
      } else {
         projects = resolveProjectMap(configuration)
      }
      
      return projects
   }
   
   protected def resolveProjectMap(BuildConfiguration configuration) {
      if(!(project =~ /\{(\w+)\}/)) {
         return ["$project": project]
      }
      
      def dereferencedProject = (project =~ /(\w)+/)[0][0]
      def projectRef = configuration.projects.getJSONObject(dereferencedProject)
      def path = projectRef.getString('path')
      return ["$dereferencedProject": path]
   }
   
//   protected List<String> resolveProjects(BuildConfiguration configuration) {
//      def paths = []
      
//      if(project == 'all') {
//         //paths += configuration.getAllProjectPaths()
//      } else {
//         paths.add(resolveProject(configuration))
//      }
      
//      return paths
//   }
   
   protected String resolveProject(BuildConfiguration configuration) {
      if(!(project =~ /\{(\w+)\}/)) {
         return project
      }
      
      def dereferencedProject = (project =~ /(\w)+/)[0][0]
      def projectRef = configuration.projects.getJSONObject(dereferencedProject)
      return projectRef.getString('path')
   }
   
   protected abstract void executeBuildStep(String projectPath)
}
