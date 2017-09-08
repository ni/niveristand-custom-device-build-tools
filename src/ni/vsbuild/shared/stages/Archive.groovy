package ni.vsbuild.shared.stages

class Archive extends AbstractStage {

  private Object buildExecutor
  
  Archive(Object script, Object buildExecutor) {
    super(script, 'Archive')
    this.buildExecutor = buildExecutor
  }

  @Override
  void execute() {
    script.stage(stageName) {
      script.echo 'Archiving build...'
      //buildExecutor.archive()
      script.echo 'Archive Complete.'
    }
  }
}
