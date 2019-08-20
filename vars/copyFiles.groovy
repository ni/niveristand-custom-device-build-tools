def call(sourceDirectory, destinationDirectory, options=[:]) {
   def copyCommand = "robocopy \"$sourceDirectory\" \"$destinationDirectory\""

   // These switches are for console output suppression so we don't get a bunch of junk
   // in our logs
   // https://ss64.com/nt/robocopy.html
   def commandSwitches = "/nfl /ndl /njh /njs /nc /ns /np"

   if(options.exclusions) {
      commandSwitches = "$commandSwitches /xf \"${options.exclusions}\""
   }

   if(options.directoryExclusions) {
      commandSwitches = "$commandSwitches /xd \"${options.directoryExclusions}\""
   }

   // If the files argument is not passed, mirror the entire source to destination
   if(options.files) {
      copyCommand = "$copyCommand \"${options.files}\""
   }
   else {
      commandSwitches = "$commandSwitches /mir"
   }

   // robocopy uses multiple return codes for success
   // https://ss64.com/nt/robocopy-exit.html
   bat "($copyCommand $commandSwitches) ^& IF %ERRORLEVEL% LSS 8 SET ERRORLEVEL=0"
}
