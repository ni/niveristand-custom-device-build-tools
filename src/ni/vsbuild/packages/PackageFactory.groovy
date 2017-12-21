package ni.vsbuild.packages

class PackageFactory implements Serializable {

   static Buildable createPackage(script, packageInfo, payloadDir) {
      def type = packageInfo.get('type')

      if(type == 'nipkg') {
         return new Nipkg(script, packageInfo, payloadDir)
      }

      script.failBuild("\'$type\' is an invalid package type.")
   }
}
