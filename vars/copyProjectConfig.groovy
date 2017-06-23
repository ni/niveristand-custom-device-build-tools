def call(projectPath, projectName){
  echo "Copying configuration file to $projectPath"
  configFileName = "$projectName.config"
  bat "xcopy /Y \"$WORKSPACE\\commonbuild\\config\\LabVIEW.exe.config\" \"$projectPath\\$configFileName\""
}
