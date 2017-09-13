package ni.vsbuild

class BuildInformation implements Serializable {
  
   private static final String DEFAULT_BUILD_STEPS_LOCATION = 'vars/buildSteps.groovy'

   public final String nodeLabel
   public final String sourceVersion
   public final List<String> lvVersions
   public final List<String> dependencies
   public final PackageType packageType
   public final String buildStepsLocation
   public final boolean officiallySupported

   public BuildInformation(String nodeLabel, String sourceVersion, List<String> lvVersions, List<String> dependencies, String buildStepsLocation, PackageType packageType, boolean officiallySupported) {
      this.nodeLabel = nodeLabel
      this.sourceVersion = sourceVersion
      this.lvVersions = lvVersions
      this.dependencies = dependencies
      this.buildStepsLocation = buildStepsLocation
      this.packageType = packageType
      this.officiallySupported = officiallySupported
   }

   public BuildInformation(String nodeLabel, String sourceVersion, List<String> lvVersions, List<String> dependencies, PackageType packageType, boolean officiallySupported) {
      this(nodeLabel, sourceVersion, lvVersions, dependencies, DEFAULT_BUILD_STEPS_LOCATION, packageType, officiallySupported)
   }

   public BuildInformation(String nodeLabel, String sourceVersion, List<String> lvVersions, String buildStepsLocation, PackageType packageType, boolean officiallySupported) {
      this(nodeLabel, sourceVersion, lvVersions, [], buildStepsLocation, packageType, officiallySupported)
   }

   public BuildInformation(String nodeLabel, String sourceVersion, List<String> lvVersions, List<String> dependencies, boolean officiallySupported) {
      this(nodeLabel, sourceVersion, lvVersions, dependencies, DEFAULT_BUILD_STEPS_LOCATION, PackageType.NIPM, officiallySupported)
   }

   public BuildInformation(String nodeLabel, String sourceVersion, List<String> lvVersions, String buildStepsLocation, boolean officiallySupported) {
      this(nodeLabel, sourceVersion, lvVersions, [], buildStepsLocation, PackageType.NIPM, officiallySupported)
   }

   public BuildInformation(String nodeLabel, String sourceVersion, List<String> lvVersions, PackageType packageType, boolean officiallySupported) {
      this(nodeLabel, sourceVersion, lvVersions, [], DEFAULT_BUILD_STEPS_LOCATION, packageType, officiallySupported)
   }

   public BuildInformation(String nodeLabel, String sourceVersion, List<String> lvVersions, boolean officiallySupported) {
      this(nodeLabel, sourceVersion, lvVersions, [], DEFAULT_BUILD_STEPS_LOCATION, PackageType.NIPM, officiallySupported)
   }

   public void printInformation(script) {
      def printString = """Building using the following BuildInformation:
         Official support: $officiallySupported
         Package type: $packageType
         Node label: $nodeLabel
         Source version: $sourceVersion
         LV versions to build: $lvVersions
         Dependencies: $dependencies
         Build steps location: $buildStepsLocation"""
      
      script.echo printString
   }

   public BuildExecutor createExecutor(script) {
      BuildExecutor executor
      
      if (officiallySupported) {
         executor = new nibuild.BuildExecutor(script, this)
      } else {
         executor = new groovy.BuildExecutor(script, this)
      }
      
      executor.loadBuildSteps(buildStepsLocation)
      return executor
   }
}
