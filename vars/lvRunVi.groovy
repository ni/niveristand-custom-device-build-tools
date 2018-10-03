def call(vi, lvVersion){
   echo "Running $vi."
   bat "labview-cli --kill --lv-ver $lvVersion \"$WORKSPACE\\niveristand-custom-device-build-tools\\lv\\build\\lvRunVi.vi\" -- \"$vi\" \"$WORKSPACE\""
}
