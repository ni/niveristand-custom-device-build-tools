class BuildInformation implements Serializable {

  public final String nodeLabel
  public final String sourceVersion
  public final String[] lvVersions
  
  public BuildInformation(String nodeLabel, String sourceVersion, String[] lvVersions) {
    this.nodeLabel = nodeLabel
    this.sourceVersion = sourceVersion
    this.lvVersions = lvVersions
  }
}
