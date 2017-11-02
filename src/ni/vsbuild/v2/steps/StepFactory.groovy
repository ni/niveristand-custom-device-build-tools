package ni.vsbuild.v2.steps

import ni.vsbuild.v2.BuildConfiguration

class StepFactory implements Serializable {
   
   static List<Step> create(script, BuildConfiguration configuration, stepList) {
      List<Step> steps = []
      def jsonSteps = configuration.getProperty(stepList).getJSONArray('steps')
      for (def jsonStep in jsonSteps) {
         Step step = StepFactory.create(script, jsonStep)
         steps.add(step)
      }
      
      return steps
   }
   
   static Step create(script, jsonStep) {
      def type = jsonStep.getString('type')
      
      if(type == 'lvBuildAll') {
         return new LvBuildAllStep(script, jsonStep)
      }
      
      if(type == 'lvBuildSpec') {
         return new LvBuildSpecStep(script, jsonStep)
      }
      
      if(type == 'lvBuildSpecAllTargets') {
         return new LvBuildSpecAllTargetsStep(script, jsonStep)
      }
      
      if(type == 'lvRunVi') {
         return new LvRunViStep(script, jsonStep)
      }
      
      script.echo "No correct type found."
      
      //*** TODO: error and return message that type is not supported ***
   }
}
