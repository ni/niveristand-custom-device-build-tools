def call(testPath, lvVersion){
   echo "Running VITester."
   branchDirectory = WORKSPACE.substring(0, WORKSPACE.lastIndexOf("\\"))
   def logFileName = getLogName(testPath)
   labviewcli("-OperationName RunVITester -TestPath \"$WORKSPACE\\$testPath\" -AdditionalOperationDirectory \"$branchDirectory\\niveristand-custom-device-testing-tools\\RunVITester\" -LogFilePath \"$WORKSPACE\\lvVITester_${logFileName}.log\"", lvVersion)
}
