package ni.vsbuild.nipm

import ni.vsbuild.shared.stages.*

class Pipeline implements Serializable {

  def script
  def stages = []
  BuildInformation buildInformation

  static builder(script, BuildInformation buildInformation) {
    return new Builder(script, buildInformation)
  }

  static class Builder implements Serializable {

    def script
    def stages = []
    BuildInformation buildInformation

    Builder(def script, BuildInformation buildInformation) {
      this.script = script
      this.buildInformation = buildInformation
    }

    def withInitialCleanStage() {
      stages << new InitialClean(script)
    }

    def withCheckoutStage() {
      stages << new Checkout(script)
    }

    def withCleanupStage() {
      stages << new Cleanup(script)
    }

    def buildGroovyPipeline() {
      withInitialCleanStage()
      withCheckoutStage()
      withCleanupStage()

      return new Pipeline(this)
    }

    def buildNiBuildPipeline() {
      return new Pipeline(this)
    }
  }

  private Pipeline(Builder builder) {
    this.script = builder.script
    this.stages = builder.stages
    this.buildInformation = builder.buildInformation
  }

  void execute() {

    for (Stage stage : stages) {
      try {
        stage.execute()
      } catch (err) {
        script.currentBuild.result = "FAILURE"
        script.error "Build failed: ${err.getMessage()}"
      }
    }
  }
}
