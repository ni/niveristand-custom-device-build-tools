def call(projectPath){
  echo "Copying configuration file for $projectPath"
  configFileName = "$projectPath" + ".config"
  bat "copy /Y \"$WORKSPACE\\commonbuild\\config\\LabVIEW.exe.config\" \"$WORKSPACE\\$configFileName\""
}
