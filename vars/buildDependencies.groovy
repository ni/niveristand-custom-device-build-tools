import hudson.AbortException

def call(buildInformation) {
  if(!buildInformation.dependencies) {
    // no dependencies need to build
    return
  }
  
  echo "Building dependencies for job ${env.JOB_NAME}."
  
  def buildVars
  
  buildInformation.dependencies.each{dependency->
    try {
      buildVars = build "../$dependency/${env.BRANCH_NAME}"
    } catch(AbortException e) {
      // check if there is a job for the current branch name
      if(e.getMessage().startsWith('No item named')) {
        echo e.getMessage()
        echo "Building branch master instead of ${env.BRANCH_NAME}."
        buildVars = build "../$dependency/master"
      } else {
        // job was found, but an exception occurred during build
        throw e
      }
    }
    
    echo "Build vars is $buildVars"
    buildVars.keySet().each{key->
      echo "$key"
    }
      
    }
  }
}
