#!/usr/bin/env groovy

// note: this script assumes that it will be invoked from another script after that script has defined the necessary parameters
// This script further assumes that Jenkins is configured (via the Pipeline Shared Libraries plugin)
// to implicitly include https://github.com/buckd/commonbuild
// For usage, see the readme in this repo

def call(nodeLabel, lvVersions, sourceVersion){
  echo 'Starting the build pipeline...'
  
  node(nodeLabel){
    bat 'set'
    def buildSteps
    def buildStepsLocation = 'vars/buildSteps.groovy'
    def exportDir = 'export'
    
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
      buildSteps = load buildStepsLocation
    }
    
    stage('Pre-Build Setup'){
      echo 'Setting up build environment...'
      lvVersions.each{lvVersion->
        buildSteps.setup(lvVersion)
      }
      echo 'Setup Complete.'
    }
    
    stage('Unit Testing'){
      echo 'Running unit tests.'
      //Make sure correct dependencies are loaded to run unit tests
      buildSteps.prepareSource(sourceVersion)
    }
    
    stage('Build'){
      echo 'Starting build...'
      
      bat "mkdir $exportDir"
      
      lvVersions.each{lvVersion->
        echo "Building for LV Version $lvVersion..."
        buildSteps.prepareSource(lvVersion)
        buildSteps.build(lvVersion)
        
        //Move build output to versioned directory
        bat "move \"${buildSteps.BUILT_DIR}\" \"$exportDir\\$lvVersion\""
        echo "Build for LV Version $lvVersion complete."
      }
      
      echo 'Build Complete.'
    }
    
    stage('Archive'){
      echo 'Archiving build...'
      def archiveDir = buildSteps.ARCHIVE_DIR
      archiveBuild(exportDir, archiveDir)
    }
    
    stage('Cleanup'){
      echo 'Cleaning up workspace after successful build.'
      deleteDir()
    }
  }
}
