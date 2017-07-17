def call(){
  echo 'Cloning commonbuild steps to workspace.'
  
  def branch = env['library.nivscommonbuild.version']
  if(!branch  || branch == null) {
    branch = 'master'
  }
  
  def organization = env.JOB_NAME.tokenize("/")[0]
  
  commonbuildDir = syncRepo("https://github.com/$organization/commonbuild", branch)
  return commonbuildDir
}
