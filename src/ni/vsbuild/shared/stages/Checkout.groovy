package ni.vsbuild.shared.stages

class Checkout extends AbstractStage {

   Checkout(Object script) {
      super(script, 'Checkout')
   }

   @Override
   void execute(executor) {
      script.stage(stageName) {
         script.echo 'Attempting to get source from repo.'
         script.timeout(time: 5, unit: 'MINUTES'){
            script.checkout(script.scm)
         }
      }
   }
}
