package ni.vsbuild.steps

import ni.vsbuild.Architecture

abstract class LvStep extends AbstractStep {

   def lvVersion
   def bitness
   def bitnessVersions
   def minimumVersion

   LvStep(script, mapStep, lvVersion) {
      super(script, mapStep)
      this.lvVersion = lvVersion
      this.bitness = mapStep.get('bitness')
      this.bitnessVersions = mapStep.get('bitness_versions')
      this.minimumVersion = mapStep.get('minimum_version')
   }

   // Return true if the current build architecture matches
   // the desired architecture for the step. If no specific
   // architecture is defined for the step, return true.
   public boolean supportedArchitecture() {
      if (!bitness) {
         return true
      }

      // If bitness is specified, check for the existence of bitness-specific
      // versions entry.
      if (bitnessVersions) {

         // If the current runtime version is not one of the bitness-specific
         // versions, ignore bitness and always build.
         if (!bitnessVersions.contains(lvVersion.lvRuntimeVersion)) {
            return true
         }
      }

      //If no version-specific entry or the current version is found in the list
      //of specific versions, check the bitness of the current version.
      return lvVersion.architecture == Architecture.bitnessToArchitecture(bitness as Integer)
   }

   public boolean atLeastMinimumVersion() {
      return lvVersion.lvRuntimeVersion >= minimumVersion
   }
}
