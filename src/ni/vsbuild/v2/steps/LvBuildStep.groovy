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
            projects.put(projectEntry, path)
         }
      } else {
         projects.add(resolveProjectMap(configuration))
      }
   }
   
   protected def resolveProjectMap(BuildConfiguration configuration) {
      if(!(project =~ /\{(\w+)\}/)) {
         return [project: project]
      }
      
      def dereferencedProject = (project =~ /(\w)+/)[0][0]
      def projectRef = configuration.projects.getJSONObject(dereferencedProject)
      return [dereferencedProject: projectRef.getString('path')]
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
