package ni.vsbuild.shared.stages

class Codegen extends AbstractStage {
  
  Codegen(Object script) {
    super(script, 'Code Generation')
  }

  @Override
  void execute(executor) {
    script.stage(stageName) {
      script.echo 'Generating code prior to build...'
      //executor.codegen()
      script.echo 'Code generation Complete.'
    }
  }
}
