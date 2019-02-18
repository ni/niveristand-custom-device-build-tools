package ni.vsbuild.packages

abstract class AbstractPackage implements Buildable {

   def script
   def type
   def payloadDir
   def lvVersion

   AbstractPackage(script, packageInfo, lvVersion) {
      this.script = script
      this.lvVersion = lvVersion
      this.type = packageInfo.get('type')
      this.payloadDir = packageInfo.get('payload_dir')
   }

   void build() {
      script.echo "Building $type package..."
      buildPackage()
      script.echo "$type package built."
   }

   abstract void buildPackage()

   protected def getBaseVersion() {
      def baseVersion = script.env.BRANCH_NAME.split("[-/]")[1]
      def versionPartCount = baseVersion.tokenize(".").size()

      def versionPartsToDisplay = 3
      for(versionPartCount; versionPartCount < versionPartsToDisplay; versionPartCount++) {
         baseVersion = "${baseVersion}.0"
      }

      return baseVersion
   }

   protected def getFullVersion() {
       def baseVersion = getBaseVersion()
       def fullVersion = "${baseVersion}.${script.currentBuild.number}"

       return fullVersion
   }
}
