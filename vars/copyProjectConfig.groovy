def call(projectPath, projectName){
  echo "Copying configuration file to $projectPath"
  configFileName = "$projectName\.config"
  echo "$configFileName"
  bat "xcopy /Y \"$WORKSPACE\\commonbuild\\config\\LabVIEW.exe.config\" \"$projectPath\\$configFileName\""
}
