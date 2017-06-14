#!/usr/bin/env groovy

def call(repo){
  echo 'Starting the pipeline build...'
  
  node('dcafbuild01'){
    //checkoutComponent(repo, env.BRANCH_NAME)
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
