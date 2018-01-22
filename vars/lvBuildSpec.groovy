def call(project, target, spec, lvVersion){
   echo "Building the build spec: $spec under target $target in project at $project"
   bat "labview-cli --kill --lv-ver $lvVersion \"$WORKSPACE\\commonbuild\\lv\\build\\lvBuildSpec.vi\" -- \"$project\" \"$target\" \"$spec\" \"$WORKSPACE\""
}
