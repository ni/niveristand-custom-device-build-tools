def call(repo, branch){
  echo ${repo}
  syncDir = ${repo} =~ /\/*$/
  echo ${syncDir}
}
