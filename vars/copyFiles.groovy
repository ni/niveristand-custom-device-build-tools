def call(sourceDirectory, destinationDirectory, files=null) {
   def copyCommand = "robocopy \"$sourceDirectory\" \"$destinationDirectory\""
   def commandSwitches = "/NFL /NDL /NJH /NJS /nc /ns /np"
   
   // If the files argument is not passed, mirror the entire source to destination
   if(!files  || files == null) {
      commandSwitches = "$commandSwitches /MIR"
   }
   else {
      copyCommand = "$copyCommand \"$files\""
   }
   
   // robocopy uses multiple return codes for success
   // https://ss64.com/nt/robocopy-exit.html
   bat "($copyCommand $commandSwitches) ^& IF %ERRORLEVEL% LSS 8 SET ERRORLEVEL=0"
}
