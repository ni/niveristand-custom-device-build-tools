package ni.vsbuild.v2.stages

class Codegen extends AbstractStage {

   Codegen(script, configuration) {
      super(script, 'Codegen', configuration)
   }
   
   void executeStage() {
      script.echo "codegen is ${configuration.codegen}"
   }
}
