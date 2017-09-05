interface PipelineBuilder {
  void setup()
  void codegen()
  void runUnitTests()
  void build()
  void archive()
  void buildPackage()
  void publish()
}
