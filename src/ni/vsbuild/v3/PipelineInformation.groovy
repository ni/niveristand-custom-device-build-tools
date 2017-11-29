package ni.vsbuild.v3

class PipelineInformation implements Serializable {
   
   private final String BUILD_INFO_STRING = "Pipeline will be run for LV versions $lvVersions and will execute on node(s) with label \'$nodeLabel\'."

   public final String nodeLabel
   public final List<String> lvVersions
   public final List<String> dependencies

   public PipelineInformation(String nodeLabel, List<String> lvVersions, List<String> dependencies = []) {
      this.nodeLabel = nodeLabel
      this.lvVersions = lvVersions
      this.dependencies = dependencies
   }

   public void printInformation(script) {
      script.echo "Pipeline will be run for LV versions $lvVersions and will execute on node(s) with label \'$nodeLabel\'."
   }
}
