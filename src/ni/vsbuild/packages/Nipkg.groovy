package ni.vsbuild.packages

class Nipkg extends AbstractPackage {

   static final String PACKAGE_DIRECTORY = "nipkg"

   private static final String CONTROL_FILE_NAME = "control"
   private static final String INSTRUCTIONS_FILE_NAME = "instructions"
   private static final String CONTROL_DIRECTORY = "control"
   private static final String DATA_DIRECTORY = "data"

   def installDestination

   Nipkg(script, packageInfo) {
      super(script, packageInfo)
      this.installDestination = packageInfo.get('install_destination')
   }

   void buildPackage(lvVersion) {
      stageFiles(lvVersion)
      
      def nipkgOutput = script.nipkgBuild(PACKAGE_DIRECTORY, PACKAGE_DIRECTORY)
      script.copyFiles(PACKAGE_DIRECTORY, "\"$payloadDir\\installer\"", nipkgOutput)
   }

   private void stageFiles(lvVersion) {
      if(!script.fileExists(PACKAGE_DIRECTORY)) {
         script.bat "mkdir \"$PACKAGE_DIRECTORY\\$CONTROL_DIRECTORY\" \"$PACKAGE_DIRECTORY\\$DATA_DIRECTORY\""
      }

      createDebianFile()
      updateControlFile(lvVersion)
      updateInstructionsFile(lvVersion)

      stagePayload(lvVersion)
   }

   private void updateControlFile(lvVersion) {
      updateBuildFile(CONTROL_FILE_NAME, CONTROL_DIRECTORY, lvVersion)
   }

   private void updateInstructionsFile(lvVersion) {
      updateBuildFile(INSTRUCTIONS_FILE_NAME, DATA_DIRECTORY, lvVersion)
   }

   private void updateBuildFile(fileName, destination, lvVersion) {
      if(!script.fileExists(fileName)) {
         return
      }

      def fileText = script.readFile(fileName)
      def updatedText = updateVersionVariables(fileText, lvVersion)

      script.writeFile file: "$PACKAGE_DIRECTORY\\$destination\\$fileName", text: updatedText
   }

   private def getBaseVersion() {
      // This is a workaround for building packages on master until we implement
      // automatic merging to release branches. Once automatic merges happen,
      // we will only need to build release- or hotfix- branch packages.
      if(script.env.BRANCH_NAME == "master") {
         return "0.0.1"
      }

      def baseVersion = script.env.BRANCH_NAME.tokenize('-')[1]
      def versionPartCount = baseVersion.tokenize(".").size()

      def versionPartsToDisplay = 3
      for(versionPartCount; versionPartCount < versionPartsToDisplay; versionPartCount++) {
         baseVersion = "${baseVersion}.0"
      }

      return baseVersion
   }

   private String updateVersionVariables(text, lvVersion) {
      def baseVersion = getBaseVersion()
      def fullVersion = "${baseVersion}.${script.currentBuild.number}+000"

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

   private void stagePayload(lvVersion) {
      def destination = updateVersionVariables(installDestination, lvVersion)
      script.copyFiles(payloadDir, "$PACKAGE_DIRECTORY\\$DATA_DIRECTORY\\$destination")
   }

   private void createDebianFile() {
      script.writeFile file: "$PACKAGE_DIRECTORY\\debian-binary", text: "2.0\n"
   }
}
