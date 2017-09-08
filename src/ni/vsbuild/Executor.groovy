package ni.vsbuild

class Executor implements Serializable {
  
  static void execute(script, BuildInformation buildInformation) {
    buildInformation.printInformation(script)
    
    def builder = nipm.Pipeline.builder(script)
    def pipeline
    
    if(!(buildInfo.officiallySupported)) {
      pipeline = builder.buildFullPipeline()
    } else {
      pipeline = builder.buildTestOnlyPipeline()
    }
    
    pipeline.execute()
  }
}
