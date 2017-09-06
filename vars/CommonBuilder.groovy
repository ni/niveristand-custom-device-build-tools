abstract class CommonBuilder implements PipelineBuilder, Serializable {

  protected final def script
  protected final BuildInformation buildInformation

  public CommonBuilder(script, buildInformation) {
    this.script = script
    this.buildInformation = buildInformation
  }

  public void setup() {
    // Ensure the VIs for executing scripts are in the workspace
    script.syncCommonbuild()
	
	builderSetup()
  }

  public void runUnitTests() {
    //Make sure correct dependencies are loaded to run unit tests
    preBuild(buildInformation.sourceVersion)
  }

  public abstract void codegen()

  public abstract void build()

  public abstract void archive()

  public abstract void buildPackage()

  public abstract void publish()

  protected abstract void builderSetup()

  protected abstract void preBuild(lvVersion)

  protected abstract void postBuild(lvVersion)

}