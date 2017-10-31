package ni.vsbuild.v2

class BuildInformation implements Serializable {
   
   private final String BUILD_INFO_STRING = """
      Building using the following BuildInformation:
         Package type: $packageType
         Node label: $nodeLabel
         LV versions to build: $lvVersions
         Dependencies: $dependencies
   """

   public final String nodeLabel
   public final List<String> lvVersions
   public final List<String> dependencies
   public final PackageType packageType

   public BuildInformation(String nodeLabel, List<String> lvVersions, List<String> dependencies, PackageType packageType) {
      this.nodeLabel = nodeLabel
      this.lvVersions = lvVersions
      this.dependencies = dependencies
      this.packageType = packageType
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
