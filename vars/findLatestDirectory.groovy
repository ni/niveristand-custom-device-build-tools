def call(baseDirectory) {
   // We can't source a Python virtualenv in one call and use it in another,
   // because Groovy will spawn a separate shell for each call.
   // Instead, build a multi-line string and execute it all at once.
   def venvDir = "env"
   def resourcesDir = "${WORKSPACE}\\niveristand-custom-device-build-tools\\resources"
   def output = bat returnStdout: true, script: """
      pip install virtualenv
      virtualenv ${venvDir}
      call ${venvDir}\\Scripts\\activate.bat
      
      python -u "${resourcesDir}/find_latest_directory.py" "$baseDirectory"
      
      call ${venvDir}\\Scripts\\deactivate.bat
      rmdir /s /q ${venvDir}
   """

   def trimmedOutput = output.trim()
   return trimmedOutput.substring(trimmedOutput.lastIndexOf("\n")).trim()
}
