package ni.vsbuild.shared.stages

class Setup extends AbstractStage {

   Setup(Object script) {
      super(script, 'Pre-Build Setup')
   }

   @Override
   void execute(executor) {
      script.stage(stageName) {
         script.echo 'Setting up build environment...'
         executor.setup()
         script.echo 'Setup Complete.'
      }
   }
}
