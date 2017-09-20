// In the future, this function should use the lvVersion and modify
// the config file so the correct version of the VeriStand assemblies
// is loaded. Right now, we're only building for 2016, so it's ok.
def call(projectPath, lvVersion){
   echo "Copying configuration file for $projectPath"
   configFileName = "$projectPath" + ".config"
   //bat "copy /Y \"$WORKSPACE\\commonbuild\\config\\LabVIEW.exe.config\" \"$WORKSPACE\\$configFileName\""
   
   def currentVersion = lvVersion as int
   def newAssemblyVersion = "${currentVersion}.0.0.0"
   
   def previousVersion = currentVersion - 1
   def oldAssemblyVersion = "0.0.0.0-${previousVersion}.9.9.9"
   
   def fileContent = readFile "commonbuild/config/LabVIEW.exe.config"
   fileContent = fileContent.replaceAll("(oldVersion=\")[^\"]+","\$1$oldAssemblyVersion")
      .replaceAll("(newVersion=\")[^\"]+","\$1$newAssemblyVersion")
   
   writeFile file: configFileName, text: fileContent
}
