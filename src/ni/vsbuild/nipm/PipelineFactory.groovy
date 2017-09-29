package ni.vsbuild.nipm

import ni.vsbuild.BuildInformation
import ni.vsbuild.Pipeline

class PipelineFactory implements Serializable {
   
   static Pipeline buildPipeline(script, BuildInformation buildInformation) {
      return groovy.Pipeline.builder(script, buildInformation).build()
   }
}
