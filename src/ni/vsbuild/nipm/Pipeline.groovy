package ni.vsbuild.nipm

import ni.vsbuild.BuildInformation
import ni.vsbuild.shared.stages.*

class Pipeline implements Serializable {

  def script
  def stages = []

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
    
    def withSetupStage() {
      stages << new Setup(script, buildInformation)
    }
    
    def withUnitTestStage() {
    }
    
    def withBuildStage() {
    }
    
    def withArchiveStage() {
    }
    
    def withPackageStage() {
    }
    
    def withPublishStage() {
    }

    def withCleanupStage() {
      stages << new Cleanup(script)
    }

    def addTestStages() {
      withInitialCleanStage()
      withCheckoutStage()
      withSetupStage()
      withUnitTestStage()
    }
    
    def addBuildStages() {
      withBuildStage()
      withArchiveStage()
      withPackageStage()
    }
    
    def buildPipeline() {
      addTestStages()
      addBuildStages()
      
      if(${env.BRANCH_NAME} == 'master') {
        withPublishStage()
      }
      
      withCleanupStage()
      
      return new Pipeline(this)
    }
  }

  private Pipeline(Builder builder) {
    this.script = builder.script
    this.stages = builder.stages
  }

  void execute() {

    script.node('dcaf') {
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
}
