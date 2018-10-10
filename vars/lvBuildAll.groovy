def call(project,lvVersion){
   echo "Building all build specs in project at $project"
   def logFileName = getLogName(project)
   labviewcli("-OperationName ExecuteAllBuildSpecs -ProjectPath \"$WORKSPACE\\$project\" -LogFilePath \"$WORKSPACE\\lvBuildAll_${logFileName}.log\"", lvVersion)
}
