package ni.vsbuild

class PipelineExecutor implements Serializable {
  
  static void execute(script, BuildInformation buildInformation) {
    buildInformation.printInformation(script)
    
    nipm.Pipeline.builder(script, buildInformation).buildPipeline().execute()
  }
}
