def call(projectPath, projectName){
  echo "Copying configuration file for $projectPath\\$projectName"
  configFileName = "$projectName" + ".config"
  bat "copy /Y \"$WORKSPACE\\commonbuild\\config\\LabVIEW.exe.config\" \"$WORKSPACE\\$projectPath\\$configFileName\""
}
