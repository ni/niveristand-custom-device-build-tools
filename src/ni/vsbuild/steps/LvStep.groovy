package ni.vsbuild.steps

import ni.vsbuild.Architecture

abstract class LvStep extends AbstractStep {

   def lvVersion
   def bitness

   LvStep(script, mapStep, lvVersion) {
      super(script, mapStep)
      this.lvVersion = lvVersion
      this.bitness = mapStep.get('bitness')
   }

   // Return true if the current build architecture matches
   // the desired architecture for the step. If no specific
   // architecture is defined for the step, return true.
   public boolean supportedArchitecture() {
      if(!bitness) {
         return true
      }

      return lvVersion.architecture == Architecture.bitnessToArchitecture(bitness as Integer)
   }
}
