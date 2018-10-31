// The bat command always returns the full command as well as the output
// when returnStdout is true. To get the actual value, trim the leading and 
// trailing whitespace, split the text to separate the command and the stdout
// value and finally index the split list.
def call(var) {
   def commandOutput = bat returnStdout: true, script: "echo off & echo %${var}% & echo on"
   def value = commandOutput.trim().split("\n")[1]
   return value
}
