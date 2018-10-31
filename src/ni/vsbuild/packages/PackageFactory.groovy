package ni.vsbuild.packages

class PackageFactory implements Serializable {

   static Buildable createPackage(script, packageInfo) {
      def type = packageInfo.get('type')

      if(type == 'nipkg') {
         return new Nipkg(script, packageInfo)
      }

      script.failBuild("\'$type\' is an invalid package type.")
   }
}
