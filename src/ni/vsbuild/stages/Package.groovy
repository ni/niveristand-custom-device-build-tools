package ni.vsbuild.stages

import ni.vsbuild.packages.Buildable
import ni.vsbuild.packages.PackageFactory

class Package extends AbstractStage {

   Package(script, configuration, lvVersion) {
      super(script, 'Package', configuration, lvVersion)
   }

   void executeStage() {
      Buildable pkg = PackageFactory.createPackage(script, configuration.packageInfo)
      pkg.build(lvVersion)
   }
}
