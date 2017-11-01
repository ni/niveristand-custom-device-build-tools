package ni.vsbuild.v2.steps

import ni.vsbuild.v2.BuildConfiguration

class BuildStepFactory implements Serializable {
   
   static Step create(script, jsonStep) {
      def type = jsonStep.getString('type')
      
      if(type == 'lvBuildAll') {
         //return new LvBuildAll(script, jsonStep)
      }
      
      if(type == 'lvBuildSpec') {
         return new build.LvBuildSpec(script, jsonStep)
      }
      
      if(type == 'lvBuildSpecAllTargets') {
         //return new LvBuildSpecAllTargets(script, jsonStep)
      }
      
      if(type == 'lvRunVi') {
         //return new LvRunVi(script, jsonStep)
      }
      
      //*** TODO: error and return message that type is not supported ***
   }
}
