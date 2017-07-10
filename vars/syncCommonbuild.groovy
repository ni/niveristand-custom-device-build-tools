def call(branch){
  echo 'Cloning commonbuild steps to workspace.'
  commonbuildDir = syncRepo('https://github.com/buckd/commonbuild', branch)
  return commonbuildDir
}
