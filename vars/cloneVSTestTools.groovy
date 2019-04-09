def call(){
   echo 'Cloning vs test tools to workspace.'

   def organization = getComponentParts()['organization']
   def branch = env."library.vs-test-tools.version"
   buildToolsDir = cloneRepo("https://github.com/$organization/niveristand-custom-device-testing-tools", branch)
   return testToolsDir
}
