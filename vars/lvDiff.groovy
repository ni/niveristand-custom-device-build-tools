// Taken from https://github.com/LabVIEW-DCAF/buildsystem/blob/master/vars/lvDiff.groovy

def call(lvVersion, diffingPicRepo, githubDiffToken) {
   echo 'Running LabVIEW diff build between origin/main and this commit'
   def diffDir = "${WORKSPACE}\\diff_dir"
   def resourcesDir = "${WORKSPACE}\\niveristand-custom-device-build-tools\\resources"
   bat "if exist ${diffDir} rmdir /s /q ${diffDir}"
   bat "mkdir ${diffDir}"

   // We can't source a Python virtualenv in one call and use it in another,
   // because Groovy will spawn a separate shell for each call.
   // Instead, build a multi-line string and execute it all at once.
   // We silence echo in some calls so as to not print out the token.
   def venvDir = "env"
   bat """
      pip install virtualenv
      virtualenv ${venvDir}
      call ${venvDir}\\Scripts\\activate.bat
      
      pip install requests
      
      python -u "${resourcesDir}/labview_diff.py" "${WORKSPACE}" "${diffDir}" ${lvVersion} --target=origin/main
      @python -u "${resourcesDir}/github_commenter.py" --token="${githubDiffToken}" --pic-dir="${diffDir}" --pull-req="${CHANGE_ID}" --info="${JOB_NAME}" --pic-repo="${diffingPicRepo}"
      
      call ${venvDir}\\Scripts\\deactivate.bat
      rmdir /s /q ${venvDir}
   """

   lvCloseLabVIEW()
}
