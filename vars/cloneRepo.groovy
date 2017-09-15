def call(repo, branch){
   echo "Cloning $repo to workspace."
   cloneDir = repo.tokenize("/").last()
   
   bat "mkdir $cloneDir"
   
   if(!branch  || branch == null){
      branch = 'master'
   }
   
   dir(cloneDir){
      git url: repo, branch: branch
   }
   
   return cloneDir
}
