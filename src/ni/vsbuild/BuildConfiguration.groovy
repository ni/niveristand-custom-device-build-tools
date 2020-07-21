package ni.vsbuild

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
   public final def test
   public final def dependencies
   public final def packageInfo
   public final def notificationInfo

   private BuildConfiguration(archive, projects, codegen, build, test, dependencies, packageInfo, notificationInfo) {
      this.archive = archive
      this.projects = projects
      this.codegen = codegen
      this.build = build
      this.test = test
      this.dependencies = dependencies
      this.packageInfo = packageInfo
      this.notificationInfo = notificationInfo
   }

   static BuildConfiguration loadString(def script, String jsonConfig, String lvVersion) {
      // Convert the JSON to HashMaps instead of using the JsonObject
      // because the Pipeline security plugin disables lots of JsonObject
      // functionality that is required for this build system
      def convertedJson = new JsonSlurperClassic().parseText(jsonConfig)

      convertedJson = replaceTags(script, convertedJson, lvVersion)

      return new BuildConfiguration(
         convertedJson.archive,
         convertedJson.projects,
         convertedJson.codegen,
         convertedJson.build,
         convertedJson.test,
         convertedJson.dependencies,
         convertedJson.package,
         convertedJson.notification)
   }

   public void printInformation(script) {
      def configurationString = """
         Build configuration is:
            Archive: $archive
            Projects: $projects
            Codegen: $codegen
            Build: $build
            Tests: $test
            Dependencies: $dependencies
            Package: $packageInfo
            Notification: $notificationInfo
         """.stripIndent()

      script.echo configurationString
   }

   public def getProjectList() {
      return getFieldList(projects)
   }

   public def getDependenciesList() {
      return getFieldList(dependencies)
   }

   private def getFieldList(def field) {
      def list = []
      for(def key : field.keySet()) {
         list.add(field.get(key))
      }

      return list
   }

   private static def replaceTags(def script, def jsonItem, def lvVersion) {
      if(jsonItem instanceof java.lang.String ||
         jsonItem instanceof groovy.lang.GString) {

         return StringSubstitution.replaceStrings(jsonItem, lvVersion)
      }

      if(jsonItem instanceof java.util.ArrayList) {
         for(def i = 0; i < jsonItem.size(); i++) {
            jsonItem[i] = replaceTags(script, jsonItem[i], lvVersion)
         }
      }

      if(jsonItem instanceof java.util.HashMap) {
         for(def key : jsonItem.keySet()) {
            jsonItem[key] = replaceTags(script, jsonItem[key], lvVersion)
         }
      }

      return jsonItem
   }
}
