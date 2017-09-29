package ni.vsbuild

class PipelineExecutor implements Serializable {

   static void execute(script, BuildInformation buildInformation) {
      buildInformation.printInformation(script)

      PipelineFactory.buildPipeline(script, buildInformation).execute()
   }
}
