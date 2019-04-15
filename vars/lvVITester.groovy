def call(testPath, lvVersion){
   echo "Running VITester."
   branchDirectory = WORKSPACE.substring(0, WORKSPACE.lastIndexOf("\\"))
   labviewcli("-OperationName RunVITester -TestPath \"$WORKSPACE\\$testPath\" -AdditionalOperationDirectory \"$branchDirectory\\niveristand-custom-device-testing-tools\\RunVITester\"", lvVersion)
}
