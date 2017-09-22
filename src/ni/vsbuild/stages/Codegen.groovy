package ni.vsbuild.stages

class Codegen extends AbstractStage {

   Codegen(Object script) {
      super(script, 'Code Generation')
   }

   @Override
   void executeStage(executor) {
      script.echo 'Generating code prior to build...'
      executor.codegen()
      script.echo 'Code generation Complete.'
   }
}
