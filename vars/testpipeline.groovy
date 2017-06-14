#!/usr/bin/env groovy

def call(repo){
  echo 'Starting the pipeline build...'
  
  node('dcafbuild01'){
    checkoutComponent(repo, env.BRANCH_NAME)
    //echo 'Checking out the component repo...'
    //checkout([$class: 'GitSCM', branches: [[name: "*/${env.BRANCH_NAME}"]], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[url: "${repo}"]]])
    echo 'Loading export.groovy...'
    load "vars/*.groovy"
    //def myexport = load 'vars/export.groovy'
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
      myexport()
      //myexport.call()
    }
    stage('Cleanup'){
      echo 'Cleaning up workspace...'
      deleteDir()
    }
  }
}
