package ni.vsbuild.shared.stages

abstract class AbstractStage implements Stage {

  def stageName
  def script

  AbstractStage(script, String stageName) {
    this.script = script
	this.stageName = stageName
  }

  abstract void execute(executor)

}
