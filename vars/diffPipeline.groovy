#!/usr/bin/env groovy

import org.jenkinsci.plugins.workflow.steps.FlowInterruptedException
import hudson.AbortException

//note: this script assumes that it will be invoked from another script after that script has defined the necessary parameters

//This script further assumes that Jenkins is configured (via the Pipeline Shared Libraries plugin) to implicitly include https://github.com/ni/niveristand-custom-device-build-tools

def call(lvVersion) {
   // Skip diffing for build which aren't pull-requests.
   // Only pull-requests will have the change ID set.
   if (!env.CHANGE_ID) {
      return
   }

   int timeoutInMinutes = 10

   node(lvVersion) {
      echo 'Starting build...'
      stage('Pre-Clean') {
         deleteDir()
      }
      stage('SCM_Checkout') {
         echo 'Attempting to get source from repo...'
         timeout(time: timeoutInMinutes, unit: 'MINUTES') {
            checkout scm
         }

         timeout(time: timeoutInMinutes, unit: 'MINUTES') {
            cloneBuildTools()
         }
      }
      // If this change is a pull request, diff vis.
      stage('Diff VIs') {
         try {
            timeout(time: 60, unit: 'MINUTES') {
               lvDiff(lvVersion, env.DIFFING_PIC_REPO, env.GITHUB_DIFF_TOKEN)
               echo 'Diff Succeeded!'
            }
         } catch (FlowInterruptedException | AbortException e) {
            // We would expect to get a FlowInterruptedException if the timeout occurs.
            // However this is only true some of the time - depending on how Jenkins
            // kills the processes, we may get an AbortException from the killed process.
            // There is no good common ancestor to catch, so we catch both explicitly.

            // There is currently (2/7/19) not a way to have a stage report
            // failure but have the pipeline report success. We could mark
            // the build as unstable, but that causes GitHub report a failure.
            // We will allow the diff to fail semi-silently since the build itself succeeded.
            print "Diffing VIs failed due to timeout"
         }
      }
      stage('Cleanup') {
         try {
            deleteDir()
         }
         catch (CompositeIOException | FileSystemException e) {
               echo "Directory cleanup failed"
         }
      }
   }
}
