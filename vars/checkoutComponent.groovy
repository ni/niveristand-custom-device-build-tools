def call(repository, branch){
  echo "Checking out branch ${branch} in repository ${repository}."
  checkout([$class: 'GitSCM', branches: [[name: "*/${branch}"]], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[url: "${repository}"]]])
}
