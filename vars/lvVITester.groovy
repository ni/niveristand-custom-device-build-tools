def call(Test_path, lvVersion){
   echo "Running VITester."
   masterDirectory = WORKSPACE.substring(0, WORKSPACE.lastIndexOf("\\"))
   labviewcli("-OperationName RunVITester -TestPath \"$WORKSPACE\\$Test_path\" -AdditionalOperationDirectory \"$masterDirectory\\niveristand-custom-device-testing-tools\\RunVITester\"", lvVersion)
}
