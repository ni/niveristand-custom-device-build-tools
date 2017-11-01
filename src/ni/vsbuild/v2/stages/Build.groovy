package ni.vsbuild.v2.stages

class Build extends AbstractStage {

   Build(script, configuration) {
      super(script, 'Build', configuration)
   }
   
   void executeStage() {
      script.echo "build is ${configuration.build}"
      def steps = configuration.build.getJSONArray('steps')
      script.echo "$steps"
      for (def step in steps) {
         def name = step.getString('name')
         def type = step.getString('type')
         script.echo "Step $name is of type $type"
      }
   }
}
