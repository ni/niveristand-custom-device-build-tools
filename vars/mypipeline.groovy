#!/usr/bin/env groovy

def call(component){
  echo 'Starting the pipeline build...'
  
  node('dcafbuild01'){
    echo 'Checking out the component repo...'
    checkout scm: [$class: 'GitSCM', branches: [[name: '*/dynamic-load']], extensions: [], submoduleCfg: [], userRemoteConfigs: [[url: 'http://github/buckd/commonbuild.git']]]
    echo 'Loading export.groovy...'
    load 'export.groovy'
    stage('Build'){
      echo 'Starting build...'
      export()
    }
  }
}
