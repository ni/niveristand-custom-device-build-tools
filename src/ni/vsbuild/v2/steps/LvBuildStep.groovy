package ni.vsbuild.v2.steps

import ni.vsbuild.v2.BuildConfiguration

abstract class LvBuildStep extends LvStep {

   def project
   def outputDir
   
   LvBuildStep(script, jsonStep, lvVersion) {
      super(script, jsonStep, lvVersion)
      this.project = jsonStep.getString('project')
      this.outputDir = jsonStep.optBoolean('output_dir')
   }
   
   void executeStep(BuildConfiguration configuration) {
      def projects = resolveProjectsMap(configuration)
      for(def key : projects.keys()) {
         def myVal = projects.get(key)
         script.echo "myVal is $myVal"
         executeBuildStep(myVal)
      }
   }
   
   protected def resolveProjectsMap(BuildConfiguration configuration) {
      def projects = [:]
      
      if(project == 'all') {
         for(def projectEntry : configuration.getProjectList()) {
            def path = projectEntry.getString('path')
            projects[projectEntry] = path
         }
      } else {
         projects = resolveProjectMap(configuration)
      }
      
      return projects
   }
   
   protected def resolveProjectMap(BuildConfiguration configuration) {
      if(!(project =~ /\{(\w+)\}/)) {
         return [project: project]
      }
      
      def dereferencedProject = (project =~ /(\w)+/)[0][0]
      def projectRef = configuration.projects.getJSONObject(dereferencedProject)
      script.echo "projectRef is $projectRef"
      def path = projectRef.getString('path')
      script.echo "path is $path"
      def returnValue = [dereferencedProject: path]
      script.echo "return value is $returnValue"
      return returnValue
   }
   
   protected List<String> resolveProjects(BuildConfiguration configuration) {
      def paths = []
      
      if(project == 'all') {
         //paths += configuration.getAllProjectPaths()
      } else {
         paths.add(resolveProject(configuration))
      }
      
      return paths
   }
   
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
