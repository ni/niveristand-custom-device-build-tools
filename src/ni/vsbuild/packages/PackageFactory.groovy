package ni.vsbuild.packages

class PackageFactory implements Serializable {

   static Buildable createPackage(script, packageInfo, lvVersion, strategy) {
      def type = packageInfo.get('type')

      if(type == 'nipkg') {
         return new Nipkg(script, packageInfo, lvVersion, strategy)
      }
      else if(type == 'zip') {
         return new Zip(script, packageInfo, lvVersion)
      }

      script.failBuild("\'$type\' is an invalid package type.")
   }
}
