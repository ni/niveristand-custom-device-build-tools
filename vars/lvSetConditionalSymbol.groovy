def call(project, symbol, value, lvVersion){
   echo "Setting the conditional symbol $symbol to $value for $project"
   bat "labview-cli --kill --lv-ver $lvVersion \"$WORKSPACE\\niveristand-custom-device-build-tools\\lv\\build\\lvSetConditionalSymbol.vi\" -- \"$project\" \"$symbol\" \"$value\" \"$WORKSPACE\""
}
