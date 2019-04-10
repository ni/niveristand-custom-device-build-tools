def call(Test_path, lvVersion){
   echo "Running VITester."
   branchDirectory = WORKSPACE.substring(0, WORKSPACE.lastIndexOf("\\"))
   labviewcli("-OperationName RunVITester -TestPath \"$WORKSPACE\\$Test_path\" -AdditionalOperationDirectory \"$branchDirectory\\niveristand-custom-device-testing-tools\\RunVITester\"", lvVersion)
}
