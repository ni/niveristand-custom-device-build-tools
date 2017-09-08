package ni.vsbuild.shared.stages

class Publish extends AbstractStage {

  private Object buildExecutor
  
  Publish(Object script, Object buildExecutor) {
    super(script, 'Publish')
    this.buildExecutor = buildExecutor
  }

  @Override
  void execute() {
    script.stage(stageName) {
      script.echo 'Publishing package...'
      //buildExecutor.publish()
      script.echo 'Publish Complete.'
    }
  }
}
