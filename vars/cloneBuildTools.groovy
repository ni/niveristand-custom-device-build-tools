def call(){
   echo 'Cloning build tools to workspace.'
   
   def organization = getComponentParts()['organization']
   def branch = env."library.vs-build-tools.version"
   Jenkins.instance.getAllItems(AbstractItem.class).each {
	println(it.fullName)
   };
   buildToolsDir = cloneRepo("https://github.com/$organization/niveristand-custom-device-build-tools", branch)
   return buildToolsDir
}
