package ni.vsbuild.shared.stages

class Cleanup extends AbstractStage {

  Cleanup(Object script) {
    super(script, 'Cleanup')
  }

  @Override
  void execute() {
    script.stage(stageName) {
      script.echo 'Cleaning up workspace after successful build.'
	  script.deleteDir()
    }
  }
}