package ni.vsbuild.shared.stages

class Build extends AbstractStage {
  
  Build(Object script) {
    super(script, 'Build')
  }

  @Override
  void execute(executor) {
    script.stage(stageName) {
      script.echo 'Starting build...'
      executor.build()
      script.echo 'Build Complete.'
    }
  }
}
