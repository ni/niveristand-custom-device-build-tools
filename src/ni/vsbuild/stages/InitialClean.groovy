package ni.vsbuild.stages

class InitialClean extends AbstractStage {

   InitialClean(Object script) {
      super(script, 'Initial Clean')
   }

   @Override
   void execute(executor) {
      script.echo 'Cleaning the workspace before building.'
      script.deleteDir()
   }
}
