def call(vi, lvVersion){
   echo "Running $vi."
   bat "labview-cli --kill --lv-ver $lvVersion \"$WORKSPACE\\commonbuild\\lv\\build\\lvRunVi.vi\" -- \"$vi\" \"$WORKSPACE\""
}
