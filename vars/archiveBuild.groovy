def call(exportDir, archiveDir){
  def archiveLocation = "$archiveDir\\$exportDir"
  //don't do this delete with the actual archive
  //this is for testing purposes only
  if(fileExists(archiveLocation)){
    bat "rmdir \"$archiveLocation\" /s /q"
  }
    bat "xcopy \"$exportDir\" \"$archiveLocation\" /e /i"
}
