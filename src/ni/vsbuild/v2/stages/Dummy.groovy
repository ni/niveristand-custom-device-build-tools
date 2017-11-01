package ni.vsbuild.v2.stages

class Dummy extends AbstractStage {

   Dummy(script) {
      super(script, 'Dummy')
   }
   
   @override
   void executeStage() {
      script.echo "This is a dummy stage for testing."
   }
}
