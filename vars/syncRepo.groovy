def call(repo, branch){
  echo "${repo}"
  //syncDir = "${repo}" =~ /\/[a-zA-Z0-9_]+$/
  syncDir = repo.tokenize("/").last()
  echo "${syncDir}"
}
