// Copies the config file specifying all of the VeriStand assemblies
// and versions, then replaces the versions based on the lvVersion
def call(projectPath, lvVersion){
   echo "Copying configuration file for $projectPath"
   configFileName = "$projectPath" + ".config"
   
   def currentVersion = lvVersion as int
   def newAssemblyVersion = "${currentVersion}.0.0.0"
   
   def previousVersion = currentVersion - 1
   def oldAssemblyVersion = "0.0.0.0-${previousVersion}.9.9.9"
   
   def fileContent = readFile "commonbuild/resources/LabVIEW.exe.config"
   fileContent = fileContent.replaceAll("(oldVersion=\")[^\"]+","\$1$oldAssemblyVersion")
      .replaceAll("(newVersion=\")[^\"]+","\$1$newAssemblyVersion")
   
   writeFile file: configFileName, text: fileContent
}
