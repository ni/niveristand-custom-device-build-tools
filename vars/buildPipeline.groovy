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
    def archiveLocation
    def buildStepsLocation = 'vars/buildSteps.groovy'
    def exportDir = 'export'
    def mybuilder = new CommonBuilder(this, lvVersions, sourceVersion)
    
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
      buildSteps = mybuilder.loadBuildSteps()
      //buildSteps = load buildStepsLocation
    }
    
    stage('Pre-Build Setup'){
      echo 'Setting up build environment...'
      
      // Ensure the VIs for executing scripts are in the workspace
      mybuilder.setup()
      
      //echo 'Syncing dependencies.'
      //buildSteps.syncDependencies()
      
      echo 'Setup Complete.'
    }
    
    stage('Unit Testing'){
      echo 'Running unit tests.'
      //Make sure correct dependencies are loaded to run unit tests
      mybuilder.runUnitTests()
      //buildSteps.prepareSource(sourceVersion)
      //buildSteps.setupLv(sourceVersion)
    }
    
    stage('Build'){
      echo 'Starting build...'
      
      bat "mkdir $exportDir"
      
      lvVersions.each{lvVersion->
        echo "Building for LV Version $lvVersion..."
        //buildSteps.prepareSource(lvVersion)
        //buildSteps.setupLv(lvVersion)
        preBuild(buildSteps, lvVersion)
        buildSteps.build(lvVersion)
        
        //Move build output to versioned directory
        bat "move \"${buildSteps.BUILT_DIR}\" \"$exportDir\\$lvVersion\""
        echo "Build for LV Version $lvVersion complete."
      }
      
      echo 'Build Complete.'
    }
    
    stage('Archive'){
      echo 'Archiving build...'
      archiveLocation = archiveBuild(exportDir, buildSteps.ARCHIVE_DIR)
    }
    
    stage('Package'){
      echo "Building NIPM package from build at $archiveLocation."
    }
    
    stage('Publish'){
      echo 'Publishing NIPM package.'
    }
    
    stage('Cleanup'){
      echo 'Cleaning up workspace after successful build.'
      deleteDir()
    }
  }
}
