package ni.vsbuild.stages

class Setup extends AbstractStage {

   Setup(Object script) {
      super(script, 'Pre-Build Setup')
   }

   @Override
   void executeStage(executor) {
      script.echo 'Setting up build environment...'
      executor.setup()
      script.echo 'Setup Complete.'
   }
}
