def call(lvVersion, diffingPicRepo, githubDiffToken) {
   echo 'Running LabVIEW diff build between origin/master and this commit'
   def diffDir = "${WORKSPACE}\\diff_dir"
   def resourcesDir = "${WORKSPACE}\\niveristand-custom-device-build-tools\\resources"
   bat "if exist ${diffDir} rd /s /q ${diffDir}"
   bat "mkdir ${diffDir}"
   def forwardDiffDir = diffDir.replace("\\", "/")
   def forwardResourcesDir = resourcesDir.replace("\\", "/")
   bat "git difftool --no-prompt --trust-exit-code --extcmd=\"python '${forwardResourcesDir}/labview_diff.py' \$LOCAL \$REMOTE ${forwardDiffDir} ${lvVersion}\" origin/master HEAD"
   // Silencing echo so as to not print out the token.
   bat "@python \"${forwardResourcesDir}/github_commenter.py\" --token=\"${githubDiffToken}\" --pic-dir=\"${forwardDiffDir}\" --pull-req=\"${CHANGE_ID}\" --info=\"${JOB_NAME}\" --pic-repo=\"${diffingPicRepo}\""
   lvCloseLabVIEW()
}
