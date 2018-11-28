#!/usr/bin/env groovy

//note: this script assumes that it will be invoked from another script after that script has defined the necessary parameters

//This script further assumes that Jenkins is configured (via the Pipeline Shared Libraries plugin) to implicitly include https://github.com/ni/niveristand-custom-device-build-tools

def call(lvVersion) {
   if (env.CHANGE_ID) {
      node(lvVersion) {
         echo 'Starting build...'
         stage('Pre-Clean') {
            //deleteDir()
         }
         stage('SCM_Checkout') {
            echo 'Attempting to get source from repo...'
            timeout(time: 5, unit: 'MINUTES') {
               //checkout scm
            }

            timeout(time: 5, unit: 'MINUTES') {
               //cloneBuildTools()
            }
         }
         // If this change is a pull request, diff vis.
         stage('Diff VIs') {
            timeout(time: 60, unit: 'MINUTES') {
               lvDiff(lvVersion, env.DIFFING_PIC_REPO, env.GITHUB_DIFF_TOKEN)
               echo 'Diff Succeeded!'
            }
         }
         stage('Cleanup') {
            //deleteDir()
            echo 'skipping cleanup'
         }
      }
   }
}
