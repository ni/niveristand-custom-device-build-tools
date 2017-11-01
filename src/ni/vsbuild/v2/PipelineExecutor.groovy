package ni.vsbuild.v2

class PipelineExecutor implements Serializable {

   static void execute(script) {
      //need to build dependencies here************
      def pipeline = new Pipeline(script)
      pipeline.execute()
      
      //def configuration = BuildConfiguration.load(script, jsonFile)
      //configuration.printInformation(script)

      //PipelineFactory.buildPipeline(script, buildInformation).execute()
   }
}
