def call(UTF_path, lvVersion){
   echo "SRPSM: Running UTF."
   labviewcli("-OperationName RunUnitTests -ProjectPath \"$WORKSPACE\\$UTF_path\" -JUnitReportPath \"$WORKSPACE\\UTF_path.log\"", lvVersion)
}
