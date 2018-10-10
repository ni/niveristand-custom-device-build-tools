def call(project, target, spec, lvVersion){
   echo "Building the build spec: $spec under target $target in project at $project"
   labviewcli("-OperationName ExecuteBuildSpec -ProjectPath \"$WORKSPACE\\$project\" -TargetName \"$target\" -BuildSpecName \"$spec\" -LogFilePath \"$WORKSPACE\\lvBuildSpec_${spec}.log\"", lvVersion)
}
