package ni.vsbuild

class Executor implements Serializable {

  private final def script
  private final BuildInformation buildInformation

  Executor(script, buildInformation) {
    this.script = script
    this.buildInformation = buildInformation
  }
  
  void execute() {
    buildInformation.printInformation(script)
    ni.vsbuild.nipm.Pipeline.builder(script).buildFullPipeline().execute()
  }
  
  static void execute(script, BuildInformation buildInformation) {
    buildInformation.printInformation(script)
    ni.vsbuild.nipm.Pipeline.builder(script).buildFullPipeline().execute()
  }
}
