def call(project, lvVersion){
   echo "Building all build specs in project at $project"
   bat "labview-cli --kill --lv-ver $lvVersion \"$WORKSPACE\\niveristand-custom-device-build-tools\\lv\\build\\lvBuildAll.vi\" -- \"$project\" \"$WORKSPACE\""
}
