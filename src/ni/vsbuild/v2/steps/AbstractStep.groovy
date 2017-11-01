package ni.vsbuild.v2.steps

import ni.vsbuild.v2.BuildConfiguration

abstract class AbstractStep implements Step {

   def script
   def name
   
   AbstractStep(script, jsonStep) {
      this.script = script
      this.name = jsonStep.getString('name')
   }
   
   abstract void execute(BuildConfiguration configuration)
}
