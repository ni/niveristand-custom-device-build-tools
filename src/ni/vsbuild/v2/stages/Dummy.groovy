package ni.vsbuild.v2.stages

class Dummy extends AbstractStage {

   Dummy(script, configuration) {
      super(script, 'Dummy', configuration)
   }
   
   void executeStage() {
      script.echo "This is a dummy stage for testing."
      script.echo "${configuration.constants}"
   }
}
