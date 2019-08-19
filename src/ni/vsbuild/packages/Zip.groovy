package ni.vsbuild.packages

class Zip extends AbstractPackage {

   Zip(script, packageInfo, lvVersion) {
      super(script, packageInfo, lvVersion)
   }

   void buildPackage(outputLocation) {
      def componentParts = script.getComponentParts()
      def repoName = componentParts['repo']
      def fullVersion = getFullVersion()
      def destination = "$outputLocation\\${repoName}_${payloadDir}_${fullVersion}.zip"
      script.zipBuild(payloadDir, destination);
   }
}
