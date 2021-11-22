package ni.vsbuild.packages

class DefaultPackageStrategy implements PackageStrategy {

   def lvVersion

   DefaultPackageStrategy(lvVersion) {
      this.lvVersion = lvVersion
   }

   def filterPackageCollection(packageCollection) {
      return packageCollection.findAll { !(it.get('multi_bitness')) \
                                          || ((it.get('multi_bitness_versions')) \
                                             && !(it.get('multi_bitness_versions').contains(lvVersion.lvRuntimeVersion)))
      }
   }

   @NonCPS
   def createNipkgPayloadMap(payloadMap) {
      return payloadMap
   }
}
