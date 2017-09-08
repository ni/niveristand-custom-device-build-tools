package ni.vsbuild

class Executor implements Serializable {
  
  static void execute(script, BuildInformation buildInformation) {
    buildInformation.printInformation(script)
    ni.vsbuild.nipm.Pipeline.builder(script).buildFullPipeline().execute()
  }
}
