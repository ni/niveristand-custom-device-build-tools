class BuildInformation implements Serializable {

  public final String nodeLabel
  public final String sourceVersion
  public final List<String> lvVersions
  public final List<String> dependencies
  
  public BuildInformation(String nodeLabel, String sourceVersion, List<String> lvVersions, List<String> dependencies) {
    this.nodeLabel = nodeLabel
    this.sourceVersion = sourceVersion
    this.lvVersions = lvVersions
    this.dependencies = dependencies
  }
}
