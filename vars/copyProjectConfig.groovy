// Copies the config file specifying all of the VeriStand assemblies
// and versions, then replaces the versions based on the lvVersion
def call(projectPath, lvVersion){
   echo "Copying configuration file for $projectPath"
   configFileName = "$projectPath" + ".config"

   def newAssemblyVersion = "${lvVersion}.0.0.0"

   def fileContent = readFile "niveristand-custom-device-build-tools/resources/LabVIEW.exe.config"
   fileContent = fileContent.replaceAll("(newVersion=\")[^\"]+","\$1$newAssemblyVersion")

   writeFile file: configFileName, text: fileContent
}
