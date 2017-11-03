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
   
   public List<String> getAllProjectPaths() {
      def paths = []
      for(def key in projects.keys()) {
         def entry = projects.getJSONObject(key)
         def path = entry.getString('path')
         paths.add(path)
      }
      
      return paths
   }
   
   private void validate() {
      if (archive && !(archive.containsKey('build_output_dir') && archive.containsKey('archive_location'))) {
         error("archive must define \'build_output_dir\' and \'archive_location\'.")
      }
   }
   
   private error(message) {
      script.currentBuild.result = "FAILURE"
      script.error "Build failed: $message"
   }
}
