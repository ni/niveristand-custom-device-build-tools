package ni.vsbuild.v2

class BuildConfiguration implements Serializable {

   private final String CONFIGURATION_STRING = """
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
   
   private BuildConfiguration(contants, mkdirectories, exports, projects, codegen, build, dependencies) {
      this.constants = constants
      this.mkdirectories = mkdirectories
      this.exports = exports
      this.projects = projects
      this.codegen = codegen
      this.build = build
      this.dependencies = dependencies
   }
   
   static BuildConfiguration load(def script, String jsonFile) {
      def constants
      def mkdirectories
      def exports
      def projects
      def codegen
      def build
      def dependencies
      
      def props = script.readJSON file: jsonFile
      script.echo props.toString()
      script.echo props.keySet().toString()
      script.echo props.keySet().contains('constants')
      
//      if (keys.cotains('constants')) {
//         constants = props.constants
//      }
      
//      if (keys.cotains('mkdirectories')) {
//         mkdirectories = props.mkdirectories
//      }
      
//      if (keys.cotains('exports')) {
//         exports = props.exports
//     }
      
//      if (keys.cotains('projects')) {
//         projects = props.projects
//      }
      
//      if (keys.cotains('codegen')) {
//         codegen = props.codegen
//      }
      
//      if (keys.cotains('build')) {
//         build = props.build
//      }
      
//      if (keys.cotains('dependencies')) {
//         dependencies = props.dependencies
//      }
      
      return new BuildConfiguration(constants, mkdirectories, exports, projects, codegen, build, dependencies)
   }
   
   public void printInformation(script) {
      script.echo CONFIGURATION_STRING
   }
}
