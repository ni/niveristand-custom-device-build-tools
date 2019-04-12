def call(){
   echo 'Cloning vs test tools to workspace.'

   def organization = getComponentParts()['organization']
   def branch = env."library.vs-test-tools.version"

   repo = "https://github.com/$organization/niveristand-custom-device-testing-tools"
   echo "Cloning $repo to $branch directory."
   testToolsDir = "..\\" + repo.tokenize("/").last()

   echo "Over-writing old test tools at $testToolsDir"
   bat "if exist \"$testToolsDir\" rmdir /S /Q \"$testToolsDir\""
   bat "mkdir $testToolsDir"

   if(!branch  || branch == null){
      branch = 'master'
   }

   dir(testToolsDir){
      git url: repo, branch: branch
   }
   return testToolsDir
}
