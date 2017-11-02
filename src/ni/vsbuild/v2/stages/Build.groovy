package ni.vsbuild.v2.stages

import ni.vsbuild.v2.steps.Step
import ni.vsbuild.v2.steps.StepFactory

class Build extends AbstractStage {

   Build(script, configuration) {
      super(script, 'Build', configuration)
   }
   
   void executeStage() {
      script.echo "build is ${configuration.build}"
      //List<Step> steps = []
      //def jsonSteps = configuration.build.getJSONArray('steps')
      //for (def jsonStep in jsonSteps) {
         //Step step = StepFactory.create(script, jsonStep)
         //steps.add(step)
      //}
      
      List<Step> steps = buildAllSteps('build')
      for(Step step in steps) {
         step.execute(configuration)
      }
   }
   
   private List<Step> buildAllSteps(String path) {
      List<Step> steps = []
      def myproperty = configuration.getProperty(path)
      script.echo "property is $myproperty"
      def jsonSteps = myproperty.getJSONArray('steps')
      for (def jsonStep in jsonSteps) {
         Step step = StepFactory.create(script, jsonStep)
         steps.add(step)
      }
   }
}
