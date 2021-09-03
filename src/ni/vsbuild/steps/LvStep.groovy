package ni.vsbuild.steps

import ni.vsbuild.Architecture

abstract class LvStep extends AbstractStep {

   def lvVersion
   def architecture

   LvStep(script, mapStep, lvVersion) {
      super(script, mapStep)
      this.lvVersion = lvVersion
      this.architecture = Architecture.bitnessToArchitecture(this.outputDir = mapStep.get('bitness'))
   }

   // Return true if the current build architecture matches
   // the desired architecture for the step. If no specific
   // architecture is defined for the step, return true.
   public boolean supportedArchitecture() {
      if(!architecture) {
         return true
      }

      return lvVersion.architecture == this.architecture
   }
}
