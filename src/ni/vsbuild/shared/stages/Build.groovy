package ni.vsbuild.shared.stages

class Build extends AbstractStage {

  private Object buildExecutor
  
  Build(Object script, Object buildExecutor) {
    super(script, 'Build')
    this.buildExecutor = buildExecutor
  }

  @Override
  void execute() {
    script.stage(stageName) {
      script.echo 'Starting build...'
      //buildExecutor.build()
      script.echo 'Build Complete.'
    }
  }
}
