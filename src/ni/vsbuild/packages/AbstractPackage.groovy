package ni.vsbuild.packages

abstract class AbstractPackage implements Buildable {

   def script
   def type
   def payloadDir
   def lvVersion

   AbstractPackage(script, packageInfo, lvVersion) {
      this.script = script
      this.lvVersion = lvVersion
      this.type = packageInfo.get('type')
      this.payloadDir = packageInfo.get('payload_dir')
   }

   void build() {
      script.echo "Building $type package..."
      buildPackage()
      script.echo "$type package built."
   }

   abstract void buildPackage()

}
