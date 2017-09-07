package ni.vsbuild

class BuildExecutor implements Serializable {

  private final def script
  private final BuildInformation buildInformation

  BuildExecutor(script, buildInformation) {
    this.script = script
    this.buildInformation = buildInformation
  }
  
  void execute() {
    buildInformation.printInformation(script)
  }
}
