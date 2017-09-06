class GroovyBuilder implements Serializable {

  private static final String EXPORT_DIR = 'export'

  private final def script
  private final BuildInformation buildInformation
  private String archiveLocation
  private def buildSteps

  public GroovyBuilder(script, buildInformation, buildStepsLocation) {
    super(script, buildInformation)
	loadBuildSteps(buildStepsLocation)
  }

  public void setup() {
    // Ensure the VIs for executing scripts are in the workspace
    script.syncCommonbuild()
	
	script.echo 'Syncing dependencies.'
    buildSteps.syncDependencies()
  }

  public void runUnitTests() {
    //Make sure correct dependencies are loaded to run unit tests
    preBuild(buildInformation.sourceVersion)
  }

  public void codegen() {
    buildSteps.codegen(buildInformation.sourceVersion)
  }

  public void build() {
    script.bat "mkdir $EXPORT_DIR"
    
    buildInformation.lvVersions.each{lvVersion->
      script.echo "Building for LV Version $lvVersion..."
      preBuild(lvVersion)
      buildStps.build(lvVersion)
      postBuild(lvVersion)
    }
  }

  public void archive() {
    archiveLocation = "${buildSteps.ARCHIVE_DIR}\\$EXPORT_DIR"
    
    //don't do this delete with the actual archive
    //this is for testing purposes only
    if(script.fileExists(archiveLocation)) {
      script.bat "rmdir \"$archiveLocation\" /s /q"
    }
    
    script.bat "xcopy \"$EXPORT_DIR\" \"$archiveLocation\" /e /i"
    setArchiveVar(archiveLocation)
  }

  public void buildPackage() {
    script.noop()
  }

  public void publish() {
    script.noop()
  }

  private void loadBuildSteps(buildStepsLocation) {
    def component = script.getComponentParts()['repo']
    script.echo "Loading build steps from $component/$buildStepsLocation"
    buildSteps = script.load buildStepsLocation
  }

  private void preBuild(lvVersion) {
    script.echo "Preparing source for execution with LV $lvVersion..."
    buildSteps.prepareSource(lvVersion)
    script.echo "Applying build configuration to LV $lvVersion..."
    buildSteps.setupLV(lvVersion)
  }

  private void postBuild(lvVersion) {
    //Move build output to versioned directory
    script.bat "move \"${buildSteps.BUILT_DIR}\" \"$EXPORT_DIR\\$lvVersion\""
    script.echo "Build for LV Version $lvVersion complete."
  }

  private void setArchiveVar(archiveLocation) {
    def component = script.getComponentParts()['repo']
    def depDir = "${component}_DEP_DIR"
    script.env[depDir] = archiveLocation
  }
}
