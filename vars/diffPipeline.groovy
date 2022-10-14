#!/usr/bin/env groovy

import org.jenkinsci.plugins.workflow.steps.FlowInterruptedException
import hudson.AbortException
import java.nio.file.FileSystemException
import jenkins.util.io.CompositeIOException
import ni.vsbuild.LabviewBuildVersion

//note: this script assumes that it will be invoked from another script after that script has defined the necessary parameters

//This script further assumes that Jenkins is configured (via the Pipeline Shared Libraries plugin) to implicitly include https://github.com/ni/niveristand-custom-device-build-tools

// The default timeout for diffing is 60 minutes. This is normally sufficient to render all VI diffs.
// For large diffs, this timeout can be overridden in the Jenkinsfile, either by modifying the Jenkinsfile
// in the repo or by setting the value when running a Jenkins replay.
def call(String lvVersion, int diffTimeout = 60) {
   // Skip diffing for build which aren't pull-requests.
   // Only pull-requests will have the change ID set.
   if (!env.CHANGE_ID) {
      return
   }

   int timeoutInMinutes = 10

   node(lvVersion) {
      echo 'Starting build...'
      stage('Pre-Clean') {
         cleanWorkspace()
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
            timeout(time: diffTimeout, unit: 'MINUTES') {
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
            cleanWorkspace()
         }
         catch (CompositeIOException | FileSystemException e) {
               echo "Directory cleanup failed"
         }
      }
   }
}

def call(HashMap<Integer, List<String>> lvVersions, int diffTimeout = 60) {
   // The diff script currently expects a 32-bit LabVIEW version.
   // Get the first 32-bit build version to use for the diff
   // by looking up the list of versions in the version map using the
   // 32-bit key and then indexing the first item in the list.
   // Ultimately, we may need to figure out a way to diff in either
   // bitness, but for now, we will keep it the same.
   def firstVersionEntry = lvVersions[64][0]
   call(firstVersionEntry, diffTimeout)
}
