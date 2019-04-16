def call(){
   echo 'Cloning test tools'

   def organization = getComponentParts()['organization']
   def branch = env."library.vs-test-tools.version"
   testToolsDir = cloneRepo("https://github.com/$organization/niveristand-custom-device-testing-tools", branch,"..\\")
   return testToolsDir
}
