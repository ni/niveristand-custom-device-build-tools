def call(vi, lvVersion){
  echo "Running the vi $vi."
  bat "labview-cli --kill --lv-ver $lvVersion \"$WORKSPACE\\commonbuild\\lv\\lvRunVi.vi\" -- \"$vi\" \"$WORKSPACE\""
}
