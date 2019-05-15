package ni.vsbuild.packages

class Nipkg extends AbstractPackage {

   static final String PACKAGE_DIRECTORY = "nipkg"

   private static final String CONTROL_FILE_NAME = "control"
   private static final String INSTRUCTIONS_FILE_NAME = "instructions"
   private static final String CONTROL_DIRECTORY = "control"
   private static final String DATA_DIRECTORY = "data"
   private static final String INSTALLER_DIRECTORY = "installer"

   def installDestination

   Nipkg(script, packageInfo, lvVersion) {
      super(script, packageInfo, lvVersion)

      // Yes, I'm calling toString() on what appears to be a string, but is not actually
      // java.lang.String. Instead, the interpolated string is a groovy.lang.GString.
      // http://docs.groovy-lang.org/latest/html/documentation/index.html#_double_quoted_string
      // There appears to be a bug in Groovy's runtime argument overloading evaluation
      // that fails to find the key when a GString is passed to getAt() instead of a String
      // https://stackoverflow.com/questions/39145121/why-i-cannot-get-exactly-the-same-gstring-as-was-put-to-map-in-groovy
      this.installDestination = packageInfo.get("${lvVersion}_install_destination".toString()) ?: packageInfo.get('install_destination')
   }

   void buildPackage() {
      stageFiles()

      def nipkgOutput = script.nipkgBuild(PACKAGE_DIRECTORY, PACKAGE_DIRECTORY)
      script.copyFiles(PACKAGE_DIRECTORY, "\"$payloadDir\\$INSTALLER_DIRECTORY\"", [files: nipkgOutput])
   }

   // This method is responsible for setting up the directory and file
   // structure required to build a File Package using nipkg.exe.
   // The structure is defined at the following link.
   // http://www.ni.com/documentation/en/ni-package-manager/18.5/manual/assemble-file-package/
   private void stageFiles() {
      if(!script.fileExists(PACKAGE_DIRECTORY)) {
         script.bat "mkdir \"$PACKAGE_DIRECTORY\\$CONTROL_DIRECTORY\" \"$PACKAGE_DIRECTORY\\$DATA_DIRECTORY\""
      }

      createDebianFile()
      updateControlFile()
      updateInstructionsFile()

      stagePayload()
   }

   private void createDebianFile() {
      script.writeFile file: "$PACKAGE_DIRECTORY\\debian-binary", text: "2.0\n"
   }

   private void updateControlFile() {
      updateBuildFile(CONTROL_FILE_NAME, CONTROL_DIRECTORY)
   }

   private void updateInstructionsFile() {
      updateBuildFile(INSTRUCTIONS_FILE_NAME, DATA_DIRECTORY)
   }

   private void updateBuildFile(fileName, destination) {
      if(!script.fileExists(fileName)) {
         return
      }

      def fileText = script.readFile(fileName)
      def updatedText = updateVersionVariables(fileText)

      script.writeFile file: "$PACKAGE_DIRECTORY\\$destination\\$fileName", text: updatedText
   }

   // The plan is to enable automatic merging from master to
   // release or hotfix branch packages and not build packages
   // for any other branches, including master. The version must
   // be appended to the release or hotfix branch name after a
   // dash (-) or slash (/).
   private String updateVersionVariables(text) {
      def baseVersion = getBaseVersion()
      def fullVersion = getFullVersion()

      def replacements = ['nipkg_version': fullVersion, 'display_version': baseVersion]
      script.versionReplacementExpressions().each { expression ->
         replacements."$expression" = lvVersion
      }

      def updatedText = text
      replacements.each {expression, value ->
         updatedText = updatedText.replaceAll("\\{$expression\\}", value)
      }

      return updatedText
   }

   private void stagePayload() {
      if(!installDestination) {
         // If installDestination is not provided, build an
         // empty package (virtual package).
         // A virtual package is useful for defining package
         // relationships without requiring a package payload.
         return
      }

      def destination = updateVersionVariables(installDestination)
      script.copyFiles(payloadDir, "$PACKAGE_DIRECTORY\\$DATA_DIRECTORY\\$destination", [exclusions: "*manifest.*"])
   }
}
