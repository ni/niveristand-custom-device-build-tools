def call(commonbuild_dir){
  commonbuild_dir = 'commonbuild'
  
  bat "mkdir ${commonbuild_dir}"
  dir(${commonbuild_dir){
    git url: 'https://github.com/buckd/commonbuild', branch: 'dynamic-load'
  }
  return commonbuild_dir
}
