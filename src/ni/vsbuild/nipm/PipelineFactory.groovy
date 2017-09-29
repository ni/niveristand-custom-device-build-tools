package ni.vsbuild.nipm

import ni.vsbuild.BuildFlow
import ni.vsbuild.BuildInformation
import ni.vsbuild.Pipeline

class PipelineFactory implements Serializable {
   
   static Pipeline buildPipeline(script, BuildInformation buildInformation) {
      if(buildInformation.buildFlow == BuildFlow.NIBUILD) {
         return nibuild.Pipeline.builder(script, buildInformation).build()
      } else {
         return groovy.Pipeline.builder(script, buildInformation).build()
      }
   }
}
