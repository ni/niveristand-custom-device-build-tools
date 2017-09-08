package ni.vsbuild.shared.stages

class Setup extends AbstractStage {

  private Object buildExecutor
  
  Setup(Object script, Object buildExecutor) {
    super(script, 'Pre-Build Setup')
    this.buildExecutor = buildExecutor
  }

  @Override
  void execute() {
    script.stage(stageName) {
      script.echo 'Setting up build environment...'
      //buildExecutor.setup()
      script.echo 'Setup Complete.'
    }
  }
}
