package ni.vsbuild.v3

import groovy.json.JsonSlurperClassic

class BuildConfiguration implements Serializable {

   private final String CONFIGURATION_STRING = """
Build configuration is:
   Archive: $archive
   Projects: $projects
   Codegen: $codegen
   Build: $build
   Dependencies: $dependencies
   Package type: $package_type
"""

   static final String STAGING_DIR = 'staging'
   
   public final def archive
   public final def projects
   public final def codegen
   public final def build
   public final def dependencies
   public final def package_type
   
   private BuildConfiguration(archive, projects, codegen, build, dependencies, package_type) {
      this.archive = archive
      this.projects = projects
      this.codegen = codegen
      this.build = build
      this.dependencies = dependencies
      this.package_type = package_type
   }
   
   static BuildConfiguration load(def script, String jsonFile) {      
      def config = script.readJSON file: jsonFile
      
      // Convert the JSON to HashMaps instead of using the JsonObject
      // because the Pipeline security plugin disables lots of JsonObject
      // functionality that is required for this build system
      def convertedJson = new JsonSlurperClassic().parseText(config.toString())
      
      return new BuildConfiguration(
         convertedJson.archive,
         convertedJson.projects,
         convertedJson.codegen,
         convertedJson.build,
         convertedJson.dependencies,
         convertedJson.package)
   }
   
   public void printInformation(script) {
      script.echo CONFIGURATION_STRING
   }
   
   public def getProjectList() {
      def projectList = []
      for(def key : projects.keySet()) {
         projectList.add(projects.get(key))
      }
      
      return projectList
   }
   
   public def getDependenciesList() {
      def dependenciesList = []
      for(def key in dependencies.keys()) {
         dependenciesList.add(dependencies.get(key))
      }
      
      return dependenciesList
   }
   
   private void validate() {
      if (archive && !(archive.containsKey('build_output_dir') && archive.containsKey('archive_location'))) {
         script.failBuild("archive must define \'build_output_dir\' and \'archive_location\'.")
      }
   }
}
