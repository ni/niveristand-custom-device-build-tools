package ni.vsbuild.packages

class Zip extends AbstractPackage {

   private static final String INSTALLER_DIRECTORY = "Built\\installer"

   Zip(script, packageInfo, lvVersion) {
      super(script, packageInfo, lvVersion)
   }

   void buildPackage() {
      def componentParts = script.getComponentParts()
      def repoName = componentParts['repo']
      def fullVersion = getFullVersion()
      def destination = "$INSTALLER_DIRECTORY\\${repoName}_${payloadDir}_${fullVersion}.zip"
      script.zipBuild(payloadDir, destination);
   }
}
