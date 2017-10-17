package ni.vsbuild

class BuildInformation implements Serializable {

   private static final String DEFAULT_BUILD_STEPS_LOCATION = 'vars/buildSteps.groovy'
   
   private final String BUILD_INFO_STRING = """
      Building using the following BuildInformation:
         Build flow: $buildFlow
         Package type: $packageType
         Node label: $nodeLabel
         LV versions to build: $lvVersions
         Dependencies: $dependencies
         Build steps location: $buildStepsLocation
   """

   public final String nodeLabel
   public final List<String> lvVersions
   public final List<String> dependencies
   public final PackageType packageType
   public final BuildFlow buildFlow
   public final String buildStepsLocation

   public BuildInformation(String nodeLabel, List<String> lvVersions, List<String> dependencies, String buildStepsLocation, PackageType packageType, BuildFlow buildFlow) {
      this.nodeLabel = nodeLabel
      this.lvVersions = lvVersions
      this.dependencies = dependencies
      this.buildStepsLocation = buildStepsLocation
      this.packageType = packageType
      this.buildFlow = buildFlow
   }

   public BuildInformation(String nodeLabel, List<String> lvVersions, List<String> dependencies, PackageType packageType, BuildFlow buildFlow) {
      BuildInformation(nodeLabel, lvVersions, dependencies, DEFAULT_BUILD_STEPS_LOCATION, packageType, buildFlow)
   }

   public BuildInformation(String nodeLabel, List<String> lvVersions, String buildStepsLocation, PackageType packageType, BuildFlow buildFlow) {
      BuildInformation(nodeLabel, lvVersions, [], buildStepsLocation, packageType, buildFlow)
   }

   public BuildInformation(String nodeLabel, List<String> lvVersions, List<String> dependencies, BuildFlow buildFlow) {
      BuildInformation(nodeLabel, lvVersions, dependencies, DEFAULT_BUILD_STEPS_LOCATION, PackageType.NIPM, buildFlow)
   }

   public BuildInformation(String nodeLabel, List<String> lvVersions, String buildStepsLocation, BuildFlow buildFlow) {
      BuildInformation(nodeLabel, lvVersions, [], buildStepsLocation, PackageType.NIPM, buildFlow)
   }

   public BuildInformation(String nodeLabel, List<String> lvVersions, PackageType packageType, BuildFlow buildFlow) {
      BuildInformation(nodeLabel, lvVersions, [], DEFAULT_BUILD_STEPS_LOCATION, packageType, buildFlow)
   }

   public BuildInformation(String nodeLabel, List<String> lvVersions, BuildFlow buildFlow) {
      BuildInformation(nodeLabel, lvVersions, [], DEFAULT_BUILD_STEPS_LOCATION, PackageType.NIPM, buildFlow)
   }

   public void printInformation(script) {
      script.echo BUILD_INFO_STRING
   }

   public BuildExecutor createExecutor(script, lvVersion) {
      BuildExecutor executor
      
      if (buildFlow == BuildFlow.NIBUILD) {
         executor = new nibuild.BuildExecutor(script, this, lvVersion)
      } else {
         executor = new groovy.BuildExecutor(script, this, lvVersion)
      }
      
      executor.loadBuildSteps(buildStepsLocation)      
      return executor
   }
}
