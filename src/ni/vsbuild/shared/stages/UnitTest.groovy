package ni.vsbuild.shared.stages

class UnitTest extends AbstractStage {

   UnitTest(Object script) {
      super(script, 'Unit Testing')
   }

   @Override
   void execute(executor) {
      script.stage(stageName) {
         script.echo 'Running unit tests...'
         executor.runUnitTests()
         script.echo 'Unit tests Complete.'
      }
   }
}
