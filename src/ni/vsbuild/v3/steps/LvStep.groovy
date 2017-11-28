package ni.vsbuild.v3.steps

abstract class LvStep extends AbstractStep {

   def lvVersion
   
   LvStep(script, jsonStep, lvVersion) {
      super(script, jsonStep)
      this.lvVersion = lvVersion
   }
}
