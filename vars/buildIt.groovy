def call(BuildInformation buildInformation) {
  if (buildInformation.buildType == BuildType.Pipeline) {
    buildPipeline(buildInformation)
  } else {
    echo "BuildType ${buildInformation.buildType} is not supported."
    currentBuild.result = "FAILURE"
  }
}
