package ni.vsbuild.packages

abstract class AbstractPackage implements Buildable {

   static final String INSTALLER_DIRECTORY = "installer"
   static final String INVALID_VERSION = "0.0.0"

   def script
   def type
   def lvVersion
   def packageOutputDir

   AbstractPackage(script, packageInfo, lvVersion) {
      this.script = script
      this.lvVersion = lvVersion
      this.type = packageInfo.get('type')
      this.packageOutputDir = packageInfo.get('package_output_dir') ?: packageInfo.get('payload_dir')
   }

   void build() {
      script.echo "Building $type package..."
      def outputLocation = "$packageOutputDir\\$INSTALLER_DIRECTORY"
      buildPackage(outputLocation)
      script.echo "$type package built."
   }

   abstract void buildPackage(outputLocation)

   // Returns files that affect the package build.
   // This is used as a heuristic for determining whether
   // to build packages for a pull request, and does not
   // need to be exhaustive. This should not include the
   // build.toml file or build output.
   String[] getConfigurationFiles() {
      return []
   }

   protected def getBaseVersion() {
      if (script.env.CHANGE_ID) {
         // Assign an invalid version to pull requests
         return INVALID_VERSION
      }

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
