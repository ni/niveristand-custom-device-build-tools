package ni.vsbuild.v2.stages

class Dummy extends AbstractStage {

   Dummy(script, configuration, lvVersion) {
      super(script, 'Dummy', configuration, lvVersion)
   }
   
   void executeStage() {
      script.echo "This is a dummy stage for testing."
   }
}
