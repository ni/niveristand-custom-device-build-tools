package ni.vsbuild

class PipelineExecutor implements Serializable {

   static void execute(script, String nodeLabel, List<String> lvVersions, List<String> dependencies = []) {
      def pipelineInformation = new PipelineInformation(nodeLabel, lvVersions, dependencies)
      pipelineInformation.printInformation(script)
      def pipeline = new Pipeline(script, pipelineInformation)
      pipeline.execute()
   }

   static void execute(script, List<String> lvVersions, List<String> dependencies = []) {
      execute(script, '', lvVersions, dependencies)
   }
}
