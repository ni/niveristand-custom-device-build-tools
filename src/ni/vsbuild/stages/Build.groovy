package ni.vsbuild.stages

class Build extends AbstractStage {

   Build(Object script) {
      super(script, 'Build')
   }

   @Override
   void executeStage(executor) {
      script.echo 'Starting build...'
      executor.build()
      script.echo 'Build Complete.'
   }
}
