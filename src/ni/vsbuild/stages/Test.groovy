package ni.vsbuild.stages

class Test extends AbstractStepStage {

   Test(script, configuration, lvVersion) {
      super(script, 'Test', configuration, lvVersion)
   }

   void executeStage() {
      executeSteps(configuration.test)
   }
}
