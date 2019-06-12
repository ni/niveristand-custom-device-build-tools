import hudson.AbortException

def call(buildInformation) {
   if(!buildInformation.dependencies) {
      // no dependencies need to build
      return
   }
   
   echo "Building dependencies for job ${env.JOB_NAME}."
   
   def dependencyBuild
   def dependencyDir
   def branchName = env.CHANGE_BRANCH ?: env.BRANCH_NAME
   
   buildInformation.dependencies.each{dependency->
      try {
         dependencyDir = "${dependency}_DEP_DIR"

         // Allow Jenkinsfile to override which dependencies to use
         if(env."$dependencyDir") {
            String nodeLabel = ''
            if (buildInformation.nodeLabel?.trim()) {
               nodeLabel = buildInformation.nodeLabel
            }

            def dependencyFound = false
            node(nodeLabel) {
               stage("Dependency Validation") {
                  def desiredDependency = env."$dependencyDir"
                  if(fileExists(desiredDependency)) {
                     echo "Using dependency $desiredDependency."
                     dependencyFound = true
                  }
                  else {
                     echo "$desiredDependency does not exist. Rebuilding $dependency."
                  }
               }
            }

            if(dependencyFound) {
               return
            }
         }

         escapedBranchName = "$branchName".replace("/", "%2F")
         dependencyBuild = build "../$dependency/${escapedBranchName}"
      } catch(AbortException e) {
         // check if there is a job for the current branch name
         if(e.getMessage().startsWith('No item named')) {
            echo e.getMessage()
            echo "Building branch master instead of $branchName."
            dependencyBuild = build "../$dependency/master"
         } else {
            // job was found, but an exception occurred during build
            throw e
         }
      }
   
      // set env vars for downstream projects to know where archives are
      def buildVariables = dependencyBuild.buildVariables
      if(buildVariables.containsKey(dependencyDir)) {
         env."$dependencyDir" = buildVariables[dependencyDir]
      }
   }
}
