package ni.vsbuild.nipm

import ni.vsbuild.shared.stages.*

class Pipeline implements Serializable {

  def script
  def stages = []

  static builder(script) {
    return new Builder(script)
  }

  static class Builder implements Serializable {

    def script
    def stages = []

    Builder(def script) {
      this.script = script
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
