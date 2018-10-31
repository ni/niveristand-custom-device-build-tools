def call(source, destination){
   def programFiles = getWindowsVar("PROGRAMFILES")

   // http://www.ni.com/documentation/en/ni-package-manager/18.5/manual/build-package-using-cli/
   def nipkgExePath = "$programFiles\\National Instruments\\NI Package Manager\\nipkg.exe"
   bat "\"$nipkgExePath\" pack \"$source\" \"$destination\""

   def nipkgOutput = bat returnStdout: true, script: "echo off & dir \"$destination\\*.nipkg\" /B & echo on"
   nipkgOutput = (nipkgOutput =~ /[\w\-\+\.]+.nipkg/)[0]
   return nipkgOutput.trim()
}
