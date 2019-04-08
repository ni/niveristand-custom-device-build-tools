def call(Test_path, lvVersion){
   echo "MBILYK: Running VITester."
   labviewcli("-OperationName RunVITester -TestPath \"$WORKSPACE\\$Test_path\"" -AdditionalOperationDirectory \"$WORKSPACE\\niveristand-custom-device-testing-tools\\RunVITester\"", lvVersion)
}
