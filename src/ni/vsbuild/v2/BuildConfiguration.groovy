package ni.vsbuild.v2

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
      script.echo "Config class is ${config.getClass()}"
      def slurper = new JsonSlurper()
      def text = slurper.parseText(config.toString())
      script.echo "Text is ${text.getClass())}"
      script.echo text
      
      return new BuildConfiguration(
         config.get('archive'),
         config.get('projects'),
         config.get('codegen'),
         config.get('build'),
         config.get('dependencies'),
         config.get('package'))
   }
   
   public void printInformation(script) {
      script.echo CONFIGURATION_STRING
   }
   
   public def getProjectList() {
      def projectList = []
      for(def key : projects.keySet()) {
         projectList.add(projects.getJSONObject(key))
      }
      
      return projectList
   }
   
   public def getDependenciesList() {
      def dependenciesList = []
      for(def key in dependencies.keys()) {
         dependenciesList.add(dependencies.getJSONObject(key))
      }
      
      return dependenciesList
   }
   
   private void validate() {
      if (archive && !(archive.containsKey('build_output_dir') && archive.containsKey('archive_location'))) {
         script.failBuild("archive must define \'build_output_dir\' and \'archive_location\'.")
      }
   }
}
