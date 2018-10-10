def call(lvVersion){
   def versionPath = env."labviewPath_${lvVersion}"
   bat "LabVIEWCLI -LabVIEWPath \"${versionPath}\" -OperationName CloseLabVIEW -LogFilePath \"$WORKSPACE\\lvCloseLabVIEW.log\" -LogToConsole false"
}
