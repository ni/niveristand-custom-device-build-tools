package ni.vsbuild.v3.steps

import ni.vsbuild.v3.BuildConfiguration

class StepFactory implements Serializable {
   
   static List<Step> create(script, BuildConfiguration configuration, stepList, lvVersion) {
      List<Step> steps = []
      def mapSteps = configuration.getProperty(stepList).get('steps')
      for (def mapStep in mapSteps) {
         Step step = StepFactory.create(script, mapStep, lvVersion)
         steps.add(step)
      }
      
      return steps
   }
   
   static Step create(script, mapStep, lvVersion) {
      def type = mapStep.get('type')
      
      if(type == 'lvBuildAll') {
         return new LvBuildAllStep(script, mapStep, lvVersion)
      }
      
      if(type == 'lvBuildSpec') {
         return new LvBuildSpecStep(script, mapStep, lvVersion)
      }
      
      if(type == 'lvBuildSpecAllTargets') {
         return new LvBuildSpecAllTargetsStep(script, mapStep, lvVersion)
      }
      
      if(type == 'lvRunVi') {
         return new LvRunViStep(script, mapStep, lvVersion)
      }
      
      if(type == 'lvSetConditionalSymbol') {
         return new LvSetConditionalSymbolStep(script, mapStep, lvVersion)
      }
      
      script.failBuild("Type \'$type\' is invalid for step \'${mapStep.get('name')}\'.")
   }
}
