def call(){
   echo 'Cloning build tools to workspace.'
   
   def organization = getComponentParts()['organization']
   def branch = env."library.vs-build-tools.version"
   echo ${branch}
   buildToolsDir = cloneRepo("https://github.com/$organization/niveristand-custom-device-build-tools", branch)
   return buildToolsDir
}
