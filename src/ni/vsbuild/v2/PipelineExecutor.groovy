package ni.vsbuild.v2

class PipelineExecutor implements Serializable {

   static void execute(script, List<String> lvVersions, List<String> dependencies = []) {
      def pipelineInformation = new PipelineInformation('veristand', lvVersions, dependencies)
      pipelineInformation.printInformation()
      
      def pipeline = new Pipeline(script, pipelineInformation)
      pipeline.execute(script)
      
      //def configuration = BuildConfiguration.load(script, jsonFile)
      //configuration.printInformation(script)

      //PipelineFactory.buildPipeline(script, buildInformation).execute()
   }
}
