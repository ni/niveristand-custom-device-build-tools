package ni.vsbuild.stages

import ni.vsbuild.packages.Buildable
import ni.vsbuild.packages.PackageFactory

class Package extends AbstractStage {

   Package(script, configuration, lvVersion) {
      super(script, 'Package', configuration, lvVersion)
   }

   void executeStage() {
      def packageInfoCollection = []

      // Developers can specify a single package [Package] or a collection of packages [[Package]].
      // Test the package information parameter and iterate as needed.
      if (configuration.packageInfo in Collection) {
         packageInfoCollection = configuration.packageInfo
      }
      else {
         packageInfoCollection.add(configuration.packageInfo)
      }

      for (def packageInfo : packageInfoCollection) {
         Buildable pkg = PackageFactory.createPackage(script, packageInfo, lvVersion)
         pkg.build()
      }
   }
}
