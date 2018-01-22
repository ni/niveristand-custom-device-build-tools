def call(project, spec, lvVersion){
   echo "Building the build spec: $spec under all targets in project at $project"
   bat "labview-cli --kill --lv-ver $lvVersion \"$WORKSPACE\\commonbuild\\lv\\build\\lvBuildSpecAllTargets.vi\" -- \"$project\" \"$spec\" \"$WORKSPACE\""
}
