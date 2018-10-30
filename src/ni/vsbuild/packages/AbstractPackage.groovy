package ni.vsbuild.packages

abstract class AbstractPackage implements Buildable {

   def script
   def type
   def payloadDir

   AbstractPackage(script, packageInfo) {
      this.script = script
      this.type = packageInfo.get('type')
      this.payloadDir = packageInfo.get('payload_dir')
   }

   void build(lvVersion) {
      script.echo "Building $type package..."
      buildPackage(lvVersion)
      script.echo "$type package built."
   }

   abstract void buildPackage(lvVersion)

}
