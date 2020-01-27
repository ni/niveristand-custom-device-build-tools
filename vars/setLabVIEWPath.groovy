def call(lvVersion) {
   def programFiles = getWindowsVar("PROGRAMFILES(x86)")
   def baseVersion = (lvVersion =~ /^[0-9]+/).getAt(0)
   env."labviewPath_${lvVersion}" = "$programFiles\\National Instruments\\LabVIEW ${baseVersion}\\LabVIEW.exe"
}
