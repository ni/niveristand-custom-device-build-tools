#!/usr/bin/env groovy

def call(component){
  echo 'Starting the pipeline build...'
  
  node('dcafbuild01'){
    echo 'Checking out the component repo...'
    checkout([$class: 'GitSCM', branches: [[name: '*/dynamic-load']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[url: "https://github.com/buckd/' +${component}"]]])
    echo 'Loading export.groovy...'
    def myexport = load 'vars/export.groovy'
    stage('Clean'){
      echo 'Cleaning'
      deleteDir()
      bat 'set'
    }
    stage('Checkout'){
      echo 'Attempting to get source from repo...'
      timeout(time: 5, unit: 'MINUTES'){
        checkout scm
      }
    }
    stage('Build'){
      echo 'Starting build...'
      myexport.call()
    }
    stage('Cleanup'){
      echo 'Cleaning up workspace...'
      deleteDir()
    }
  }
}
