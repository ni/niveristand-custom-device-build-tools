package ni.vsbuild

interface BuildExecutor extends Serializable {
   void setup()
   void runUnitTests()
   void codegen()
   void build()
   void archive()
   void buildPackage()
   void publish()
}
