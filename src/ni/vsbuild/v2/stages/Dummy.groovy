package ni.vsbuild.v2.stages

class Checkout extends AbstractStage {

   Checkout(script) {
      super(script, 'Dummy')
   }
   
   @override
   void executeStage() {
      script.echo "This is a dummy stage for testing."
   }
}
