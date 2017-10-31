package ni.vsbuild.v2

class BuildConfiguration implements Serializable {

   private final String CONFIGURATION_STRING = """
Constants: ${constants}.toString()
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
      def keys = props.keySet()
      script.echo keys.toString()
      
      if (keys.contains('constants')) {
         constants = props.get('constants')
         if(constants) {
            script.echo 'Constants was set'
            script.echo ${constants}.toString()
         }
      }
      
//      if (keys.contains('mkdirectories')) {
//         mkdirectories = props.mkdirectories
//      }
      
//      if (keys.contains('exports')) {
//         exports = props.exports
//     }
      
//      if (keys.contains('projects')) {
//         projects = props.projects
//      }
      
//      if (keys.contains('codegen')) {
//         codegen = props.codegen
//      }
      
//      if (keys.contains('build')) {
//         build = props.build
//      }
      
//      if (keys.contains('dependencies')) {
//         dependencies = props.dependencies
//      }
      
      return new BuildConfiguration(constants, mkdirectories, exports, projects, codegen, build, dependencies)
   }
   
   public void printInformation(script) {
      script.echo CONFIGURATION_STRING
   }
}
