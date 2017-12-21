package ni.vsbuild.packages

class Nipkg extends AbstractPackage {

   def pkgVersion
   def maintainer
   def description
   def homepage
   def displayName
   def eulaDependency
   def dependencies
   
   Nipkg(script, packageInfo, payloadDir) {
      super(script, packageInfo, payloadDir)
      this.pkgVersion = packageInfo.get('version')
      this.maintainer = packageInfo.get('maintainer')
      this.description = packageInfo.get('description')
      this.homepage = packageInfo.get('homepage')
      this.displayName = packageInfo.get('display_name')
      this.eulaDependency = packageInfo.get('eula_dependency')
      this.dependencies = packageInfo.get('dependencies')
   }

   void buildPackage() {
      def packageInfo = """
         Building package $name from $payloadDir
         Package version: $pkgVersion
         Description: $description
         Homepage: $homepage
         Display name: $displayName
         Eula dependency: $eulaDependency
         Dependencies: $dependencies
         """.stripIndent()
      
      script.echo packageInfo
   }
}
