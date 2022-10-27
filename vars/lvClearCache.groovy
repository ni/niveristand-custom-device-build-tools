def call(lvVersion){
   echo "Clearing LabVIEW cache."
   def vi = "niveristand-custom-device-build-tools\\lv\\operations\\Utilities\\ClearCache.vi"
   def logFileName = getLogName(vi)
   labviewcli("-OperationName SecureRunVI -VIPath \"$WORKSPACE\\$vi\" -LogFilePath \"$WORKSPACE\\lvRunVi_${logFileName}.log\"", lvVersion)
}
