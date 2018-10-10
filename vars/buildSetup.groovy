def call(lvVersion) {
   def programFiles = bat returnStdout: true, script: "echo off & set PROGRAMFILES(x86) & echo on"
   programFiles = programFiles.split('=')[1].trim()
   env.labviewPath = "$programFiles\\National Instruments\\LabVIEW ${lvVersion}\\LabVIEW.exe"
   bat "niveristand-custom-device-build-tools\\resources\\buildSetup.bat"
}
