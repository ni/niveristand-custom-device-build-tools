def call(lvVersion) {
   def programFiles = getWindowsVar("PROGRAMFILES(x86)")
   env."labviewPath_${lvVersion}" = "$programFiles\\National Instruments\\LabVIEW ${lvVersion}\\LabVIEW.exe"
   bat "niveristand-custom-device-build-tools\\resources\\buildSetup.bat"
}
