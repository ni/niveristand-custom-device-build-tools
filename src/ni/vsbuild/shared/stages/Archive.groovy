package ni.vsbuild.shared.stages

class Archive extends AbstractStage {
  
  Archive(Object script) {
    super(script, 'Archive')
  }

  @Override
  void execute(executor) {
    script.stage(stageName) {
      script.echo 'Archiving build...'
      //executor.archive()
      script.echo 'Archive Complete.'
    }
  }
}
