class BuildInformation implements Serializable {

  public final String nodeLabel
  public final String sourceVersion
  public final List<String> lvVersions
  public final List<String> dependencies
  
  public Map<String, String> depDirs = [:]
  
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
    def printString = """Building using the following BuildInformation:
      Node label: $nodeLabel
      Source version: $sourceVersion
      LV versions to build: $lvVersions
      Dependencies: $dependencies"""
    
    script.echo printString
  }
  
  public void addDependencyArchive(buildVariables, dependencyDir) {
    if(buildVariables.containsKey(dependencyDir)) {
      depDirs[dependencyDir] = buildVariables[dependencyDir]
    }
  }
}
