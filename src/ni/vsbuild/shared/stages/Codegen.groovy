package ni.vsbuild.shared.stages

class Codegen extends AbstractStage {

  private Object buildExecutor
  
  Codegen(Object script, Object buildExecutor) {
    super(script, 'Code Generation')
    this.buildExecutor = buildExecutor
  }

  @Override
  void execute() {
    script.stage(stageName) {
      script.echo 'Generating code prior to build...'
      //buildExecutor.codegen()
      script.echo 'Code generation Complete.'
    }
  }
}
