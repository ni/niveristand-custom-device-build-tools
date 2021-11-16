package ni.vsbuild.packages

class PostArchivePackageStrategy implements PackageStrategy {

   def lvVersion

   PostArchivePackageStrategy(lvVersion) {
      this.lvVersion = lvVersion
   }

   def filterPackageCollection(packageCollection) {
      return packageCollection.findAll { it.get('multi_bitness') \
                                          &&  (!(it.get('multi_bitness_versions')) \
                                             || (it.get('multi_bitness_versions').contains(lvVersion.lvRuntimeVersion)))
      }
   }
}
