package ni.vsbuild.v2

class BuildConfiguration implements Serializable {

   private final String CONFIGURATION_STRING = """
Build configuration is:
   Paths: $paths of type ${paths.getClass()}
   Mkdirs: $mkdirectories
   Exports: $exports
   Projects: $projects
   Codegen: $codegen
   Build: $build
   Dependencies: $dependencies
   Package type: $package_type
"""

   public final def paths
   public final def mkdirectories
   public final def exports
   public final def projects
   public final def codegen
   public final def build
   public final def dependencies
   public final def package_type
   
   private BuildConfiguration(paths, mkdirectories, exports, projects, codegen, build, dependencies, package_type) {
      this.paths = paths
      this.mkdirectories = mkdirectories
      this.exports = exports
      this.projects = projects
      this.codegen = codegen
      this.build = build
      this.dependencies = dependencies
      this.package_type = package_type
   }
   
   static BuildConfiguration load(def script, String jsonFile) {      
      def config = script.readJSON file: jsonFile
      
      return new BuildConfiguration(
         config.get('paths'),
         config.get('mkdirectories'),
         config.get('exports'),
         config.get('projects'),
         config.get('codegen'),
         config.get('build'),
         config.get('dependencies'),
         config.get('package'))
   }
   
   public void printInformation(script) {
      script.echo CONFIGURATION_STRING
   }
   
   private void validate() {
      if (!(paths.containsKey('BUILT_DIR') && paths.containsKey('ARCHIVE_DIR')) {
         error("paths must define \'BUILT_DIR\' and \'ARCHIVE_DIR\'.")
      }
   }
   
   private error(message) {
      script.currentBuild.result = "FAILURE"
      script.error "Build failed: $message"
   }
}
