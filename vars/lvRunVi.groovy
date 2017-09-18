def call(vi, lvVersion){
   echo "Running $vi."
   bat "labview-cli --kill --lv-ver $lvVersion \"$WORKSPACE\\commonbuild\\lv\\lvRunVi.vi\" -- \"$vi\" \"$WORKSPACE\""
}
