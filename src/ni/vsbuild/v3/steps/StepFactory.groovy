package ni.vsbuild.v2.steps

import ni.vsbuild.v2.BuildConfiguration

class StepFactory implements Serializable {
   
   static List<Step> create(script, BuildConfiguration configuration, stepList, lvVersion) {
      List<Step> steps = []
      def jsonSteps = configuration.getProperty(stepList).getJSONArray('steps')
      for (def jsonStep in jsonSteps) {
         Step step = StepFactory.create(script, jsonStep, lvVersion)
         steps.add(step)
      }
      
      return steps
   }
   
   static Step create(script, jsonStep, lvVersion) {
      def type = jsonStep.getString('type')
      
      if(type == 'lvBuildAll') {
         return new LvBuildAllStep(script, jsonStep, lvVersion)
      }
      
      if(type == 'lvBuildSpec') {
         return new LvBuildSpecStep(script, jsonStep, lvVersion)
      }
      
      if(type == 'lvBuildSpecAllTargets') {
         return new LvBuildSpecAllTargetsStep(script, jsonStep, lvVersion)
      }
      
      if(type == 'lvRunVi') {
         return new LvRunViStep(script, jsonStep, lvVersion)
      }
      
      if(type == 'lvSetConditionalSymbol') {
         return new LvSetConditionalSymbolStep(script, jsonStep, lvVersion)
      }
      
      script.failBuild("Type \'$type\' is invalid for step \'${jsonStep.getString('name')}\'.")
   }
}
