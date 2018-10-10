def call(project, spec, lvVersion){
   echo "Building the build spec: $spec under all targets in project at $project"
   labviewcli("-OperationName ExecuteBuildSpecAllTargets -ProjectPath \"$WORKSPACE\\$project\" -BuildSpecName \"$spec\" -LogFilePath \"$WORKSPACE\\lvBuildSpecAllTargets_${spec}.log\"", lvVersion)
}
