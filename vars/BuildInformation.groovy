class BuildInformation implements Serializable {

  private static final String COMMON_BUILD_CLASS = 'CommonBuilder'
  
  public final String nodeLabel
  public final String sourceVersion
  public final List<String> lvVersions
  public final List<String> dependencies
  
  private final String builderClass
  
  public BuildInformation(String nodeLabel, String sourceVersion, List<String> lvVersions, List<String> dependencies, String builderClass) {
    this.nodeLabel = nodeLabel
    this.sourceVersion = sourceVersion
    this.lvVersions = lvVersions
    this.dependencies = dependencies
    this.builderClass = builderClass
  }
  
  public BuildInformation(String nodeLabel, String sourceVersion, List<String> lvVersions, List<String> dependencies) {
    this(nodeLabel, sourceVersion, lvVersions, dependencies, COMMON_BUILD_CLASS)
  }
  
  public BuildInformation(String nodeLabel, String sourceVersion, List<String> lvVersions, String builderClass) {
    this(nodeLabel, sourceVersion, lvVersions, [], builderClass)
  }
  
  public BuildInformation(String nodeLabel, String sourceVersion, List<String> lvVersions) {
    this(nodeLabel, sourceVersion, lvVersions, [], COMMON_BUILD_CLASS)
  }
  
  public void printInformation(script) {
    def printString = """Building using the following BuildInformation:
      Node label: $nodeLabel
      Source version: $sourceVersion
      LV versions to build: $lvVersions
      Dependencies: $dependencies"""
    
    script.echo printString
  }
  
  public def createBuilder(script) {
    return new CommonBuilder(script, this)
  }
}
