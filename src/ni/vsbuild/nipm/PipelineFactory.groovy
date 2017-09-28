package ni.vsbuild.nipm

import ni.vsbuild.BuildInformation

class PipelineFactory implements Serializable {
   
   static ni.vsbuild.Pipeline buildPipeline(script, BuildInformation buildInformation) {
      return Pipeline.builder(script, buildInformation).buildPipeline()
   }
}
