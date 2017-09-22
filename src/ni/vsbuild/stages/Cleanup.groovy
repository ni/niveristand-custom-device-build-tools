package ni.vsbuild.stages

class Cleanup extends AbstractStage {

   Cleanup(Object script) {
      super(script, 'Cleanup')
   }

   @Override
   void executeStage(executor) {
      script.echo 'Cleaning up workspace after successful build.'
      script.deleteDir()
   }
}
