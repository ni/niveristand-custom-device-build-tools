package ni.vsbuild.nipm

class PipelineFactory implements Serializable {
   
   static Pipeline buildPipeline(script, BuildInformation buildInformation) {
      return Pipeline.builder(script, buildInformation).buildPipeline()
   }
}
