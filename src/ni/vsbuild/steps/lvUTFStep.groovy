package ni.vsbuild.steps

import ni.vsbuild.BuildConfiguration

class LvUTFStep extends LvStep {

   def vi

   LvUTFStep(script, mapStep, lvVersion) {
      script.echo "UTF test echo"
   }

   void executeStep(BuildConfiguration configuration) {
      script.lvUTF(vi, lvVersion)
   }
}
