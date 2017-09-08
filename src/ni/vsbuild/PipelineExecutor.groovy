package ni.vsbuild

class PipelineExecutor implements Serializable {
  
  static void execute(script, BuildInformation buildInformation) {
    buildInformation.printInformation(script)
    
    def builder = nipm.Pipeline.builder(script, buildInformation)
    builder.buildPipeline().execute()
  }
}
