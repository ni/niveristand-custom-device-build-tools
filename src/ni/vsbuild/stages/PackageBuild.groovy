package ni.vsbuild.stages

class PackageBuild extends AbstractStage {

   PackageBuild(Object script) {
      super(script, 'Package')
   }

   @Override
   void execute(executor) {
      script.echo 'Building package...'
      executor.buildPackage()
      script.echo 'Package Complete.'
   }
}
