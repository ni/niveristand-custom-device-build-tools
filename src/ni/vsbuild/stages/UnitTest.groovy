package ni.vsbuild.stages

class UnitTest extends AbstractStage {

   UnitTest(Object script) {
      super(script, 'Unit Testing')
   }

   @Override
   void executeStage(executor) {
      script.echo 'Running unit tests...'
      executor.runUnitTests()
      script.echo 'Unit tests Complete.'
   }
}
