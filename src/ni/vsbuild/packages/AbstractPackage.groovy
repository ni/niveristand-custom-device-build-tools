package ni.vsbuild.packages

abstract class AbstractPackage implements Buildable {

   static final String INSTALLER_DIRECTORY = "installer"

   def script
   def type
   def payloadDir
   def lvVersion
   def packageOutputDir

   AbstractPackage(script, packageInfo, lvVersion) {
      this.script = script
      this.lvVersion = lvVersion
      this.type = packageInfo.get('type')
      this.payloadDir = packageInfo.get('payload_dir')
      this.packageOutputDir = packageInfo.get('package_output_dir') ?: packageInfo.get('payload_dir')
   }

   void build() {
      script.echo "Building $type package..."
      def outputLocation = "$packageOutputDir\\$INSTALLER_DIRECTORY"
      buildPackage(outputLocation)
      script.echo "$type package built."
   }

   abstract void buildPackage(outputLocation)

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
