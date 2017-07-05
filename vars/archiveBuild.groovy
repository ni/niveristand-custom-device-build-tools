def call(exportDir){
    def archiveDir = "${buildSteps.ARCHIVE_DIR}\\$exportDir"
  //don't do this delete with the actual archive
  //this is for testing purposes only
  if(fileExists(archiveDir)){
    bat "rmdir \"$archiveDir\" /s /q"
  }
    bat "xcopy \"$exportDir\" \"$archiveDir\" /e /i"
}
