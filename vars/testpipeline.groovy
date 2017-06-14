#!/usr/bin/env groovy

//note: this script assumes that it will be invoked from another script after that script has defined the necessary parameters

//These are: [none] at the moment

//This script further assumes that Jenkins is configured (via the Pipeline Shared Libraries plugin) to implicitly include https://github.com/buckd/commonbuild
//This script also requires the calling component to include a vars/buildSteps.groovy file that defines a function, build()

def call(){
  echo 'Starting the pipeline build...'
  
  node('dcafbuild01'){
    bat 'set'
    def buildSteps
    
    stage('Clean'){
      echo 'Cleaning'
      deleteDir()
    }
    stage('Checkout'){
      echo 'Attempting to get source from repo...'
      timeout(time: 5, unit: 'MINUTES'){
        checkout scm
      }
      echo 'Loading component build steps...'
      buildSteps = load 'vars/buildSteps.groovy'
    }
    stage('Build'){
      echo 'Starting build...'
      buildSteps.build()
    }
    stage('Cleanup'){
      echo 'Cleaning up workspace...'
      deleteDir()
    }
  }
}
