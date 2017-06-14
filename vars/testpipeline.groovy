#!/usr/bin/env groovy

def call(component){
  echo 'Starting the pipeline build...'
  
  node('dcafbuild01'){
    echo 'Checking out the component repo...'
    checkout([$class: 'GitSCM', branches: [[name: '*/dynamic-load']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[url: 'https://github.com/buckd/componentbuild']]])
    echo 'Loading export.groovy...'
    def myexport = load 'vars/export.groovy'
    stage('Clean'){
      echo 'Cleaning'
      deleteDir()
    }
    stage('Build'){
      echo 'Starting build...'
      myexport.call()
    }
  }
}
