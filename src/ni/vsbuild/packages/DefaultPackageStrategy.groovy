package ni.vsbuild.packages

class DefaultPackageStrategy implements PackageStrategy {

   def lvVersion

   DefaultPackageStrategy(lvVersion) {
      this.lvVersion = lvVersion
   }

   // Returns all packages with a configuration where
   // multi_bitness is not defined OR
   // if multi_bitness is defined, but the current LabVIEW version
   // does not match any of the versions specified for multi-bitness packaging.
   def filterPackageCollection(packageCollection) {
      return packageCollection.findAll { !(it.get('multi_bitness')) \
                                          || ((it.get('multi_bitness_versions')) \
                                             && !(it.get('multi_bitness_versions').contains(lvVersion.lvRuntimeVersion)))
      }
   }

   def getOutputDirectory(script, outputDir) {
      return outputDir
   }

   def createNipkgPayloadMap(script, payloadMap, outputDir) {
      return payloadMap
   }
}
