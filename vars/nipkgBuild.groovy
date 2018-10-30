def call(source, destination){
   def programFiles = bat returnStdout: true, script: "echo off & set PROGRAMFILES & echo on"
   programFiles = programFiles.split('=')[1].trim()
   programFiles = programFiles.split("\n")[0]
   
   def nipkgExePath = "$programFiles\\National Instruments\\NI Package Manager\\nipkg.exe"
   bat "\"$nipkgExePath\" pack \"$source\" \"$destination\""
   
   def nipkgOutput = bat returnStdout: true, script: "echo off & dir \"$destination\\*.nipkg\" /B & echo on"
   nipkgOutput = (nipkgOutput =~ /[\w\-\+\.]+.nipkg/)[0]
   return nipkgOutput.trim()
}
