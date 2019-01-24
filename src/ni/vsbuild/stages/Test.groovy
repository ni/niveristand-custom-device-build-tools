package ni.vsbuild.stages

class Test extends AbstractStepStage {

   Test(script, configuration, lvVersion) {
      script.echo "SRPSM: New test object"
      super(script, 'Test', configuration, lvVersion)
   }

   void executeStage() {
      this.script.echo "SRPSM: Test executeStage() method"
   }
}
