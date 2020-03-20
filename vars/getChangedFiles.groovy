import groovy.json.JsonSlurperClassic

// The bat command always returns the full command as well as the output
// when returnStdout is true. To get the actual value, trim the leading and 
// trailing whitespace, split the text to separate the command and stdout.
def call() {
   def resourcesDir = "${WORKSPACE}\\niveristand-custom-device-build-tools\\resources"

   def commandOutput = bat returnStdout: true, script: "python ${resourcesDir}/get_changed_files.py --target origin/${env.CHANGE_TARGET}"
   changedFiles = commandOutput.trim().split("\n")[1]
   return new JsonSlurperClassic().parseText(changedFiles)
}
