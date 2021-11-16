package ni.vsbuild.stages

import ni.vsbuild.packages.Buildable
import ni.vsbuild.packages.PackageFactory
import ni.vsbuild.packages.PackageStrategy
import ni.vsbuild.packages.DefaultPackageStrategy

class Package extends AbstractStage {

   private def packages
   private PackageStrategy strategy

   Package(script, configuration, lvVersion, strategy) {
      super(script, 'Package', configuration, lvVersion)
      this.strategy = strategy
   }

   Package(script, configuration, lvVersion) {
      super(script, 'Package', configuration, lvVersion)
      this.strategy = new DefaultPackageStrategy()
   }

   void executeStage() {
      for (def pkg : getPackages()) {
         pkg.build()
      }
   }

   def getPackages() {
      // https://groovy-lang.org/operators.html#_direct_field_access_operator
      if (!this.@packages) {
         createPackages()
      }

      return this.@packages
   }

   boolean stageRequired() {
      def packageInfoCollection = getPackageInfoCollection()
      return !packageInfoCollection.isEmpty()
   }

   private void createPackages() {
      def packageInfoCollection = getPackageInfoCollection()

      this.@packages = []
      for (def packageInfo : packageInfoCollection) {
         Buildable pkg = PackageFactory.createPackage(script, packageInfo, lvVersion)
         this.@packages.add(pkg)
      }
   }

   private def getPackageInfoCollection() {
      def packageInfoCollection = []
      // Developers can specify a single package [Package] or a collection of packages [[Package]].
      // Test the package information parameter and iterate as needed.
      if (configuration.packageInfo instanceof Collection) {
         packageInfoCollection = configuration.packageInfo
      }
      else {
         packageInfoCollection.add(configuration.packageInfo)
      }

      packageInfoCollection = strategy.filterPackageCollection(packageInfoCollection)
      return packageInfoCollection
   }
}
