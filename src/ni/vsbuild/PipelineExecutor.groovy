package ni.vsbuild

class PipelineExecutor implements Serializable {

   static void execute(script, String nodeLabel, HashMap<Integer, List<String>> lvVersions, List<String> dependencies = []) {
      def pipelineInformation = new PipelineInformation(nodeLabel, getLvBuildVersions(lvVersions), dependencies)
      pipelineInformation.printInformation(script)
      def pipeline = new Pipeline(script, pipelineInformation)
      pipeline.execute()
   }

   static void execute(script, HashMap<Integer, List<String>> lvVersions, List<String> dependencies = []) {
      execute(script, '', lvVersions, dependencies)
   }

   static void execute(script, String nodeLabel, List<String> lvVersions, List<String> dependencies = []) {
      execute(script, nodeLabel, buildDefaultMap(lvVersions), dependencies)
   }

   static void execute(script, List<String> lvVersions, List<String> dependencies = []) {
      execute(script, '', lvVersions, dependencies)
   }

   private static HashMap<Integer, List<String>> buildDefaultMap(List<String> lvVersions) {
      return [(LabviewBuildVersion.DEFAULT_LABVIEW_BITNESS) : lvVersions]
   }

   private static List<LabviewBuildVersion> getLvBuildVersions(HashMap<Integer, List<String>> lvVersions) {
      def lvBuildVersions = []
      lvVersions.each{entry ->
         lvVersions[entry.key].each{value ->
            lvBuildVersions << new LabviewBuildVersion(value, entry.key)}}
      return lvBuildVersions
   }
}
