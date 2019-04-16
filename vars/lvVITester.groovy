def call(testPath, lvVersion){
   echo "Running VITester."
   baseDirectory = WORKSPACE.substring(0, WORKSPACE.lastIndexOf("\\"))
   def logFileName = getLogName(testPath)
   labviewcli("-OperationName RunVITester -TestPath \"$WORKSPACE\\$testPath\" -AdditionalOperationDirectory \"$baseDirectory\\niveristand-custom-device-testing-tools\\RunVITester\" -LogFilePath \"$WORKSPACE\\lvVITester_${logFileName}.log\"", lvVersion)
}
