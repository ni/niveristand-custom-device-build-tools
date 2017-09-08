package ni.vsbuild.shared.stages

class PackageBuild extends AbstractStage {

  private Object buildExecutor
  
  PackageBuild(Object script, Object buildExecutor) {
    super(script, 'Package')
    this.buildExecutor = buildExecutor
  }

  @Override
  void execute() {
    script.stage(stageName) {
      script.echo 'Building package...'
      //buildExecutor.buildPackage()
      script.echo 'Package Complete.'
    }
  }
}
