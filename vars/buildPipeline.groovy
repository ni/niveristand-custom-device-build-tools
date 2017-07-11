#!/usr/bin/env groovy

// note: this script assumes that it will be invoked from another script after that script has defined the necessary parameters
// This script further assumes that Jenkins is configured (via the Pipeline Shared Libraries plugin)
// to implicitly include https://github.com/buckd/commonbuild
// For usage, see the readme in this repo

def call(BuildInformation buildInformation) {
  echo 'Starting the build pipeline...'
  
  node(buildInformation.nodeLabel){
    echo "Environment before build:"
    bat 'set'
    def builder = new CommonBuilder(this, buildInformation)
    
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
      builder.loadBuildSteps()
    }
    
    stage('Pre-Build Setup'){
      echo 'Setting up build environment...'
      builder.setup()      
      echo 'Setup Complete.'
    }
    
    stage('Unit Testing'){
      echo 'Running unit tests.'
      builder.runUnitTests()
      echo 'Unit tests complete.'
    }
    
    stage('Build'){
      echo 'Starting build...'      
      builder.build()      
      echo 'Build Complete.'
    }
    
    stage('Archive'){
      echo 'Archiving build...'
      builder.archive()
      echo 'Archive complete.'
    }
    
    stage('Package'){
      echo 'Building NIPM package...'
      builder.buildPackage()
      echo 'Package complete.'
    }
    
    stage('Publish'){
      echo 'Publishing NIPM package.'
      builder.publish()
      echo 'Publish complete.'
    }
    
    stage('Cleanup'){
      echo 'Cleaning up workspace after successful build.'
      deleteDir()
    }
  }
}
