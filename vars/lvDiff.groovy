def call(lvVersion, diffingPicRepo = null) {
   echo 'Running LabVIEW diff build between origin/master and this commit'
   if (!diffingPicRepo) {
      diffingPicRepo = env.DIFFING_PIC_REPO
   }
   def diffDir = "${WORKSPACE}\\diff_dir"
   def resourcesDir = "${WORKSPACE}\\niveristand-custom-device-build-tools\\resources"
   bat "if exist ${diffDir} rd /s /q ${diffDir}"
   bat "mkdir ${diffDir}"
   bat "git difftool --no-prompt --extcmd=\"'${resourcesDir}\\labview-diff.bat' \$LOCAL \$REMOTE diff_dir ${lvVersion}\" origin/master HEAD"
   // Silencing echo so as to not print out the token.
   bat "@python \"${resourcesDir}\\github_commenter.py\" --token=\"${GITHUB_DIFF_TOKEN}\" --pic-dir=\"${diffDir}\" --pull-req=\"${CHANGE_ID}\" --info=\"${JOB_NAME}\" --pic-repo=\"${diffingPicRepo}\""
}
