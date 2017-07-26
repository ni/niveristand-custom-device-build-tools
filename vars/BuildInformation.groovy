class BuildInformation implements Serializable {
  
  private static final String DEFAULT_BUILD_STEPS_LOCATION = 'vars/buildSteps.groovy'
  
  public final String nodeLabel
  public final String sourceVersion
  public final List<String> lvVersions
  public final List<String> dependencies
  
  private final String buildStepsLocation
  
  public BuildInformation(String nodeLabel, String sourceVersion, List<String> lvVersions, List<String> dependencies, String buildStepsLocation) {
    this.nodeLabel = nodeLabel
    this.sourceVersion = sourceVersion
    this.lvVersions = lvVersions
    this.dependencies = dependencies
    this.buildStepsLocation = buildStepsLocation
  }
  
  public BuildInformation(String nodeLabel, String sourceVersion, List<String> lvVersions, List<String> dependencies) {
    this(nodeLabel, sourceVersion, lvVersions, dependencies, DEFAULT_BUILD_STEPS_LOCATION)
  }
  
  public BuildInformation(String nodeLabel, String sourceVersion, List<String> lvVersions, String buildStepsLocation) {
    this(nodeLabel, sourceVersion, lvVersions, [], buildStepsLocation)
  }
  
  public BuildInformation(String nodeLabel, String sourceVersion, List<String> lvVersions) {
    this(nodeLabel, sourceVersion, lvVersions, [], DEFAULT_BUILD_STEPS_LOCATION)
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
    def builder = new CommonBuilder(script, this)
    builder.loadBuildSteps(buildStepsLocation)
    return builder
  }
}
