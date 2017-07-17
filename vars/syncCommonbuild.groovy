def call(){
  echo 'Cloning commonbuild steps to workspace.'
  def libraryVersion = 'library.nivscommonbuild.version'
  def branch = "${env.$libraryVersion}"
  echo branch
  if(!branch) {
    branch = 'master'
  }
  
  commonbuildDir = syncRepo('https://github.com/buckd/commonbuild', branch)
  return commonbuildDir
}
