def call(project, spec, lvVersion){
  echo "Building the build spec: $spec under all targets in project at $project"
  bat "labview-cli --kill --lv-ver $lvVersion \"$WORKSPACE\\commonbuild\\lv\\lvBuildAllTargets.vi\" -- \"$project\" \"$spec\" \"$WORKSPACE\""
}
