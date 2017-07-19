def call(){
  echo 'Cloning commonbuild steps to workspace.'
  
  def organization = env.JOB_NAME.tokenize("/")[0]
  def branch = env['library.nivscommonbuild.version']  
  commonbuildDir = syncRepo("https://github.com/$organization/commonbuild", branch)
  return commonbuildDir
}
