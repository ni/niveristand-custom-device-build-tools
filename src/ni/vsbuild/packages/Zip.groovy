package ni.vsbuild.packages

class Zip extends AbstractPackage {

   Zip(script, packageInfo, lvVersion) {
      super(script, packageInfo, lvVersion)
   }

   void buildPackage() {
       // zip -r <name of package file> <folder>
       script.echo "Zipping the thingies"
   }
}

