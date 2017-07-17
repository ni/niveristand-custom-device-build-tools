def call(buildInformation) {
  buildInformation.dependencies.each{dependency->
    try {
      build "../$dependency/test"
    } catch(AbortException e) {
      if(e.getMessage().startsWith('No item named') {
        build "../$dependency/${env.BRANCH_NAME}"
      } else {
        throw e
      }
    }
  }
}
