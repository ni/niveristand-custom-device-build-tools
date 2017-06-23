def call(projectPath, projectName){
  echo "Copying configuration file to $projectPath"
  configFileName = "$projectName" + ".config"
  echo "$configFileName"
  bat "copy /Y \"$WORKSPACE\\commonbuild\\config\\LabVIEW.exe.config\" \"$WORKSPACE\\$projectPath\\$configFileName\""
}
