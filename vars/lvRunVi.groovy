def call(vi){
   echo "Running $vi."
   def logFileName = getLogName(vi)
   labviewcli("-OperationName SecureRunVI -VIPath \"$WORKSPACE\\$vi\" -LogFilePath \"$WORKSPACE\\lvRunVi_${logFileName}.log\"")
}
