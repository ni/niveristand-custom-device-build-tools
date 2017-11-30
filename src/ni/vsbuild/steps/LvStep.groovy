package ni.vsbuild.steps

abstract class LvStep extends AbstractStep {

   def lvVersion

   LvStep(script, mapStep, lvVersion) {
      super(script, mapStep)
      this.lvVersion = lvVersion
   }
}
