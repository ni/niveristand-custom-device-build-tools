package ni.vsbuild.nipm

import ni.vsbuild.Pipeline
import ni.vsbuild.BuildInformation

class PipelineFactory implements Serializable {
   
   static Pipeline buildPipeline(script, BuildInformation buildInformation) {
      return Pipeline.builder(script, buildInformation).buildPipeline()
   }
}
