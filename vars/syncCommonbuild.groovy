def call(branch){
  commonbuildDir = 'commonbuild'
  bat "mkdir ${commonbuildDir}"
  
  if(!branch){
    branch = 'master'
  }
  
  dir("${commonbuildDirr}"){
    git url: 'https://github.com/buckd/commonbuild', branch: ${branch}
  }
  
  return commonbuildDir
}
