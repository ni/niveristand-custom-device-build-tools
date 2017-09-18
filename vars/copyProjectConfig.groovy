// In the future, this function should use the lvVersion and modify
// the config file so the correct version of the VeriStand assemblies
// is loaded. Right now, we're only building for 2016, so it's ok.
def call(projectPath, lvVersion){
   echo "Copying configuration file for $projectPath"
   configFileName = "$projectPath" + ".config"
   bat "copy /Y \"$WORKSPACE\\commonbuild\\config\\LabVIEW.exe.config\" \"$WORKSPACE\\$configFileName\""
}
