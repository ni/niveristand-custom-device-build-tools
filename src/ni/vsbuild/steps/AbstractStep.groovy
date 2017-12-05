package ni.vsbuild.steps

import ni.vsbuild.BuildConfiguration

abstract class AbstractStep implements Step {

   def script
   def name

   AbstractStep(script, mapStep) {
      this.script = script
      this.name = mapStep.get('name')
   }

   void execute(BuildConfiguration configuration) {
      script.echo "Step $name beginning..."
      executeStep(configuration)
      script.echo "Step $name complete."
   }

   abstract void executeStep(BuildConfiguration configuration)

}
