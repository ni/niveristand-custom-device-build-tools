def call(project, symbol, value, lvVersion){
   echo "Setting the conditional symbol $symbol to $value for $project"
   labviewcli("-OperationName SetConditionalSymbol -ProjectPath \"$WORKSPACE\\$project\" -Symbol \"$symbol\" -Value \"$value\" -LogFilePath \"$WORKSPACE\\lvSetConditionalSymbol_${symbol}.log\"", lvVersion)
}
