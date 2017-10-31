package ni.vsbuild.v2

class BuildConfiguration implements Serializable {

   private final String CONFIGURATION_STRING = """
Build configuration is:
   Constants: $constants
   Mkdirs: $mkdirectories
   Exports: $exports
   Projects: $projects
   Codegen: $codegen
   Build: $build
   Dependencies: $dependencies
"""

   public final def constants
   public final def mkdirectories
   public final def exports
   public final def projects
   public final def codegen
   public final def build
   public final def dependencies
   
   private BuildConfiguration(constants, mkdirectories, exports, projects, codegen, build, dependencies) {
      this.constants = constants
      this.mkdirectories = mkdirectories
      this.exports = exports
      this.projects = projects
      this.codegen = codegen
      this.build = build
      this.dependencies = dependencies
   }
   
   static BuildConfiguration load(def script, String jsonFile) {      
      def props = script.readJSON file: jsonFile
      
      return new BuildConfiguration(
         props.get('constants'),
         props.get('mkdirectories'),
         props.get('exports'),
         props.get('projects'),
         props.get('codegen'),
         props.get('build'),
         props.get('dependencies'))
   }
   
   public void printInformation(script) {
      script.echo CONFIGURATION_STRING
   }
}
