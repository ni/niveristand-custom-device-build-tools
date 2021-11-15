package ni.vsbuild.packages

class DefaultPackageStrategy implements PackageStrategy {

   void filterPackageCollection(packageCollection) {
      packageCollection.removeAll { it.get('multi_bitness') }
   }
}
