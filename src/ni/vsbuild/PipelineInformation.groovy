package ni.vsbuild

class PipelineInformation implements Serializable {

   public final String nodeLabel
   public final List<LabviewBuildVersion> lvVersions
   public final List<String> dependencies

   public PipelineInformation(String nodeLabel, List<LabviewBuildVersion> lvVersions, List<String> dependencies = []) {
      this.nodeLabel = nodeLabel
      this.lvVersions = lvVersions
      this.dependencies = dependencies
   }

   public void printInformation(script) {
      String infoString = "Pipeline will be run for LV versions $lvVersions"
      if(nodeLabel?.trim()) {
         infoString = "$infoString and will execute on node(s) with label \'$nodeLabel\'."
      }
      
      script.echo "$infoString"
   }  
}
