package ni.vsbuild.nipm

import ni.vsbuild.BuildInformation

class BuilderFactory implements Serializable {
   
   static def createBuilder(script, BuildInformation buildInformation) {
      if(buildInformation.officiallySupported) {
         return new nibuild.PipelineBuilder(script, buildInformation)
      } else {
         return new groovy.PipelineBuilder(script, buildInformation)
      }
   }
}
