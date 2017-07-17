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
  
  public BuildInformation(String nodeLabel, String sourceVersion, List<String> lvVersions) {
    this(nodeLabel, sourceVersion, lvVersions, [])
  }
  
  public void printInformation(script) {
    script.echo "Node label is \"$nodeLabel\"
    script.echo "Source version is $sourceVersion"
    script.echo "LV versions to build are"
    lvVersions.each{version->
      script.echo "-$version"
    }
    script.echo "Dependencies are"
    dependencies.each{dependency->
      script.echo "-$dependency"
    }
  }
}
