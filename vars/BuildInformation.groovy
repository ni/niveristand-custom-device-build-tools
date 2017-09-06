class BuildInformation implements Serializable {
  
  private static final String DEFAULT_BUILD_STEPS_LOCATION = 'vars/buildSteps.groovy'
  
  public final String nodeLabel
  public final String sourceVersion
  public final List<String> lvVersions
  public final List<String> dependencies
  public final BuildType buildType
  
  private final String buildStepsLocation
  
  public BuildInformation(String nodeLabel, String sourceVersion, List<String> lvVersions, List<String> dependencies, String buildStepsLocation, BuildType buildType) {
    this.nodeLabel = nodeLabel
    this.sourceVersion = sourceVersion
    this.lvVersions = lvVersions
    this.dependencies = dependencies
    this.buildStepsLocation = buildStepsLocation
    this.buildType = buildType
  }
  
  public BuildInformation(String nodeLabel, String sourceVersion, List<String> lvVersions, List<String> dependencies, BuildType buildType) {
    this(nodeLabel, sourceVersion, lvVersions, dependencies, DEFAULT_BUILD_STEPS_LOCATION, buildType)
  }
  
  public BuildInformation(String nodeLabel, String sourceVersion, List<String> lvVersions, String buildStepsLocation, BuildType buildType) {
    this(nodeLabel, sourceVersion, lvVersions, [], buildStepsLocation, buildType)
  }
  
  public BuildInformation(String nodeLabel, String sourceVersion, List<String> lvVersions, List<String> dependencies) {
    this(nodeLabel, sourceVersion, lvVersions, dependencies, DEFAULT_BUILD_STEPS_LOCATION, BuildType.Groovy)
  }
  
  public BuildInformation(String nodeLabel, String sourceVersion, List<String> lvVersions, String buildStepsLocation) {
    this(nodeLabel, sourceVersion, lvVersions, [], buildStepsLocation, BuildType.Groovy)
  }
  
  public BuildInformation(String nodeLabel, String sourceVersion, List<String> lvVersions, BuildType buildType) {
    this(nodeLabel, sourceVersion, lvVersions, [], DEFAULT_BUILD_STEPS_LOCATION, buildType)
  }
  
  public BuildInformation(String nodeLabel, String sourceVersion, List<String> lvVersions) {
    this(nodeLabel, sourceVersion, lvVersions, [], DEFAULT_BUILD_STEPS_LOCATION, BuildType.Groovy)
  }
  
  public void printInformation(script) {
    def printString = """Building using the following BuildInformation:
      Build type: $buildType
      Node label: $nodeLabel
      Source version: $sourceVersion
      LV versions to build: $lvVersions
      Dependencies: $dependencies
      Build steps location: $buildStepsLocation"""
    
    script.echo printString
  }
  
  public def createBuilder(script) {
    def builder
    if (buildType == BuildType.Groovy) {
      builder = new GroovyBuilder(script, this, buildStepsLocation)
    } else {
      script.echo "Build type $buildType not implemented. Using Groovy build."
      builder = new GroovyBuilder(script, this, buildStepsLocation)
    }
    return builder
  }
}
