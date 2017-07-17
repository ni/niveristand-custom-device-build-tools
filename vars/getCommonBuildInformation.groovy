def call(dependencies) {
  String nodeLabel = 'dcaf'
  List<String> lvVersions = ["2016"]
  String sourceVersion = '2016'

  return new BuildInformation(nodeLabel, sourceVersion, lvVersions, dependencies)
}
