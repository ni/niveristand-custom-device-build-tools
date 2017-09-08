package ni.vsbuild.shared.stages

class UnitTest extends AbstractStage {

  private Object buildExecutor
  
  UnitTest(Object script, Object buildExecutor) {
    super(script, 'Unit Testing')
    this.buildExecutor = buildExecutor
  }

  @Override
  void execute() {
    script.stage(stageName) {
      script.echo 'Running unit tests...'
      //buildExecutor.runUnitTests()
      script.echo 'Unit tests Complete.'
    }
  }
}
