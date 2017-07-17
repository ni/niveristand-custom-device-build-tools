def call(buildInformation) {
  buildInformation.dependencies.each{dependency->
    try {
      build "../$dependency/test"
    } catch(AbortException e) {
      if(e.getMessage().startsWith('No item named')) {
        echo e.getMessage()
        echo 'Trying build again.'
        build "../$dependency/${env.BRANCH_NAME}"
      } else {
        echo 'Did not catch correctly. Throwing.'
        throw e
      }
    }
  }
}
