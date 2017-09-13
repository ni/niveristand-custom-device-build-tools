package ni.vsbuild.shared.stages

class Publish extends AbstractStage {
  
  Publish(Object script) {
    super(script, 'Publish')
  }

  @Override
  void execute(executor) {
    script.stage(stageName) {
      script.echo 'Publishing package...'
      //executor.publish()
      script.echo 'Publish Complete.'
    }
  }
}
