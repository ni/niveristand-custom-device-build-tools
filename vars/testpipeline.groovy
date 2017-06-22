#!/usr/bin/env groovy

//note: this script assumes that it will be invoked from another script after that script has defined the necessary parameters

//These are: nodeLabel

//This script further assumes that Jenkins is configured (via the Pipeline Shared Libraries plugin) to implicitly include https://github.com/buckd/commonbuild
//This script also requires the calling component to include a vars/buildSteps.groovy file that defines a function, build()

BUILD_STEPS_PATH = 'vars/buildSteps.groovy'

def call(nodeLabel, lvVersion){
  echo 'Starting the testpipeline...'
  
  node(nodeLabel){
    bat 'set'
    def buildSteps
    
    stage('Initial Clean'){
      echo 'Cleaning the workspace before building.'
      deleteDir()
    }
    
    stage('Checkout'){
      echo 'Attempting to get source from repo.'
      timeout(time: 5, unit: 'MINUTES'){
        checkout scm
      }
      
      //Load buildSteps here so they can be used by any subsequent stages
      echo 'Loading component build steps.'
      buildSteps = load BUILD_STEPS_PATH
    }
    
    stage('Pre-Build Setup'){
      echo 'Setting up build environment...'
      buildSteps.setup(lvVersion)
      echo 'Setup Complete.'
    }
    
    stage('Build'){
      echo 'Starting build...'
      buildSteps.build(lvVersion)
      echo 'Build Complete.'
    }
    
    stage('Cleanup'){
      echo 'Cleaning up workspace after successful build.'
      //deleteDir()
    }
  }
}
