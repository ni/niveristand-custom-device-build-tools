package ni.vsbuild.nipm

import ni.vsbuild.Pipeline

class PipelineFactory implements Serializable {
   
   static Pipeline buildPipeline(script, BuildInformation buildInformation) {
      return Pipeline.builder(script, buildInformation).buildPipeline()
   }
}
