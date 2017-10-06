package ni.vsbuild.stages

class Archive extends AbstractStage {

   Archive(Object script) {
      super(script, 'Archive')
   }

   @Override
   void executeStage(executor) {
      script.echo 'Archiving build...'
      executor.archive()
      script.echo 'Archive Complete.'
   }
}
