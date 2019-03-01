import hudson.AbortException

def call(buildInformation) {
   if(!buildInformation.dependencies) {
      // no dependencies need to build
      return
   }
   
   echo "Building dependencies for job ${env.JOB_NAME}."
   
   def dependencyBuild
   
   buildInformation.dependencies.each{dependency->
      try {
         escapedBranchName = "${env.BRANCH_NAME}".replace("/", "%2F")
         dependencyBuild = build "../$dependency/${escapedBranchName}"
      } catch(AbortException e) {
         // check if there is a job for the current branch name
         if(e.getMessage().startsWith('No item named')) {
            echo e.getMessage()
            echo "Building branch master instead of ${env.BRANCH_NAME}."
            dependencyBuild = build "../$dependency/master"
         } else {
            // job was found, but an exception occurred during build
            throw e
         }
      }
   
      // set env vars for downstream projects to know where archives are
      def buildVariables = dependencyBuild.buildVariables
      def dependencyDir = "${dependency}_DEP_DIR"
      if(buildVariables.containsKey(dependencyDir)) {
         env."$dependencyDir" = buildVariables[dependencyDir]
      }
   }
}
