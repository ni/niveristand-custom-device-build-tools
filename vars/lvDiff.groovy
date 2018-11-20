// Taken from https://github.com/LabVIEW-DCAF/buildsystem/blob/master/vars/lvDiff.groovy

def call(lvVersion, diffingPicRepo, githubDiffToken) {
   echo 'Running LabVIEW diff build between origin/master and this commit'
   def diffDir = "${WORKSPACE}\\diff_dir"
   def resourcesDir = "${WORKSPACE}\\niveristand-custom-device-build-tools\\resources"
   bat "if exist ${diffDir} rd /s /q ${diffDir}"
   bat "mkdir ${diffDir}"
   bat "python -u \"${resourcesDir}/labview_diff.py\" \"${WORKSPACE}\" \"${diffDir}\" ${lvVersion} --target=origin/master"
   // Silencing echo so as to not print out the token.
   bat "@python -u \"${resourcesDir}/github_commenter.py\" --token=\"${githubDiffToken}\" --pic-dir=\"${diffDir}\" --pull-req=\"${CHANGE_ID}\" --info=\"${JOB_NAME}\" --pic-repo=\"${diffingPicRepo}\""
   lvCloseLabVIEW()
}
