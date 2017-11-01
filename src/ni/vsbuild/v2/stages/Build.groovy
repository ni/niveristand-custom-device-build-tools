package ni.vsbuild.v2.stages

class Build extends AbstractStage {

   Build(script, configuration) {
      super(script, 'Build', configuration)
   }
   
   void executeStage() {
      script.echo "build is ${configuration.build}"
      def steps = configuration.build.getJSONArray('steps')
      script.echo "$steps"
   }
}
