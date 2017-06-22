def call(branch){
  commonbuildDir = 'commonbuild'
  bat "mkdir ${commonbuildDir}"
  
  if(!branch){
    branch = 'master'
  }
  
  dir(commonbuildDir){
    git url: 'https://github.com/buckd/commonbuild', branch: branch
  }
  
  syncRepo('https://github.com/buckd/commonbuild', 'testbranch')
  
  return commonbuildDir
}
