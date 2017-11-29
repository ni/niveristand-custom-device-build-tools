package ni.vsbuild.v3

// Use JsonSlurperClassic instead of JsonSlurper because the classic
// version uses HashMaps which are serializable. The newer version
// returns LazyMaps, which are not serializable, so can not be used
// in the Jenkins pipeline
import groovy.json.JsonSlurperClassic

class BuildConfiguration implements Serializable {

   static final String STAGING_DIR = 'staging'
   
   public final def archive
   public final def projects
   public final def codegen
   public final def build
   public final def dependencies
   public final def packageType
   
   private BuildConfiguration(archive, projects, codegen, build, dependencies, packageType) {
      this.archive = archive
      this.projects = projects
      this.codegen = codegen
      this.build = build
      this.dependencies = dependencies
      this.packageType = packageType
   }
   
   static BuildConfiguration load(def script, String jsonFile) {      
      def config = script.readJSON file: jsonFile
      
      // Convert the JSON to HashMaps instead of using the JsonObject
      // because the Pipeline security plugin disables lots of JsonObject
      // functionality that is required for this build system
      //def convertedJson = new JsonSlurperClassic().parseText(config.toString())
      def convertedJson = new HashMap(config)
      
      return new BuildConfiguration(
         convertedJson.archive,
         convertedJson.projects,
         convertedJson.codegen,
         convertedJson.build,
         convertedJson.dependencies,
         convertedJson.package)
   }
   
   public void printInformation(script) {
      def configurationString = """
         Build configuration is:
            Archive: $archive
            Projects: $projects
            Codegen: $codegen
            Build: $build
            Dependencies: $dependencies
            Package type: $packageType
         """.stripIndent()
      
      script.echo configurationString
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
}
