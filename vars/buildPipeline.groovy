#!/usr/bin/env groovy

// note: this script assumes that it will be invoked from another script after that script has defined the necessary parameters
// This script further assumes that Jenkins is configured (via the Pipeline Shared Libraries plugin)
// to implicitly include https://github.com/buckd/commonbuild
// For usage, see the readme in this repo

def call(BuildInformation buildInformation) {
  echo 'Starting the build pipeline...'
  buildInformation.printInformation(this)
  
  // build dependencies before starting this pipeline
  buildDependencies(buildInformation)
  
  node(buildInformation.nodeLabel){
    echo "Environment before build:"
    bat 'set'
    def builder
    
    stage('Initial Clean'){
      echo 'Cleaning the workspace before building.'
      deleteDir()
    }
    
    stage('Checkout'){
      echo 'Attempting to get source from repo.'
      timeout(time: 5, unit: 'MINUTES'){
        checkout scm
      }
      
      //create builder after source has been cloned
      //because builder constructor needs build steps
      //from source location
      //builder = new CommonBuilder(this, buildInformation)
      builder = buildInformation.createBuilder(this)
      builder.loadBuildSteps()
    }
    
    stage('Pre-Build Setup'){
      echo 'Setting up build environment...'
      builder.setup()      
      echo 'Setup Complete.'
    }
    
    stage('Code Generation'){
      echo 'Generating code prior to build...'
      builder.codegen()
      echo 'Code generation complete.'
    }
    
    stage('Unit Testing'){
      echo 'Running unit tests...'
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
      echo 'Publishing NIPM package...'
      builder.publish()
      echo 'Publish complete.'
    }
    
    stage('Cleanup'){
      echo 'Cleaning up workspace after successful build.'
      deleteDir()
    }
  }
}
