#!/usr/bin/env groovy

//note: this script assumes that it will be invoked from another script after that script has defined the necessary parameters

//These are: nodeLabel

//This script further assumes that Jenkins is configured (via the Pipeline Shared Libraries plugin) to implicitly include https://github.com/buckd/commonbuild
//This script also requires the calling component to include a vars/buildSteps.groovy file that defines the functions setup() and build()

def call(nodeLabel, lvVersions){
  echo 'Starting the build pipeline...'
  
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
      buildSteps = load 'vars/buildSteps.groovy'
    }
    
    stage('Pre-Build Setup'){
      echo 'Setting up build environment...'
      lvVersions.each{lvVersion->
        buildSteps.setupLv(lvVersion)
      }
      buildSteps.prepareSource()
      echo 'Setup Complete.'
    }
    
    stage('Unit Testing'){
      echo 'Running unit tests.'
    }
    
    stage('Build'){
      echo 'Starting build...'
      lvVersions.each{lvVersion->
        echo "Building for LV Version $lvVersion..."
        buildSteps.build(lvVersion)
        def outputdir = buildSteps.BUILT_DIR
        echo "Output dir is $outputdir."
        echo "Build for LV Version $lvVersion complete."
      }
      echo 'Build Complete.'
    }
    
    stage('Archive'){
      echo 'Archiving build...'
      buildSteps.archive()
    }
    
    stage('Cleanup'){
      echo 'Cleaning up workspace after successful build.'
      //deleteDir()
    }
  }
}
