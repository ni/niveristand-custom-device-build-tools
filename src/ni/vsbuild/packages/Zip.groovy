package ni.vsbuild.packages

class Zip extends AbstractPackage {

   def payloadDir

   Zip(script, packageInfo, lvVersion) {
      super(script, packageInfo, lvVersion)
      this.payloadDir = packageInfo.get('payload_dir')
   }

   void buildPackage(outputLocation) {
      def componentParts = script.getComponentParts()
      def repoName = componentParts['repo']
      def fullVersion = getFullVersion()
      def destination = "$outputLocation\\${repoName}_${payloadDir}_${fullVersion}.zip"
      script.zipBuild(payloadDir, destination);
   }
}
