package ni.vsbuild.v2

class PipelineExecutor implements Serializable {

   static void execute(script, List<String> lvVersions) {
      def pipelineInformation = new PipelineInformation('veristand', lvVersions)
      def pipeline = new Pipeline(script, pipelineInformation)
      pipeline.execute()
      
      //def configuration = BuildConfiguration.load(script, jsonFile)
      //configuration.printInformation(script)

      //PipelineFactory.buildPipeline(script, buildInformation).execute()
   }
}
