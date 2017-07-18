class CommonBuilder implements Serializable {
  
  private static final String EXPORT_DIR = 'export'
  private static final String BUILD_STEPS_LOCATION = 'vars/buildSteps.groovy'
  
  private def script
  private BuildInformation buildInformation
  private String archiveLocation
  private def buildSteps
  
  public CommonBuilder(script, buildInformation) {
    this.script = script
    this.buildInformation = buildInformation
  }
  
  public void loadBuildSteps() {
    buildSteps = script.load BUILD_STEPS_LOCATION
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
      buildSteps.build(lvVersion)
      postBuild(lvVersion)
    }
  }
  
  public void archive() {
    archiveLocation = "${buildSteps.ARCHIVE_DIR}\\$EXPORT_DIR"
  
    //don't do this delete with the actual archive
    //this is for testing purposes only
    if(script.fileExists(archiveLocation)){
      script.bat "rmdir \"$archiveLocation\" /s /q"
    }
  
    script.bat "xcopy \"$EXPORT_DIR\" \"$archiveLocation\" /e /i"
  }
  
  public void buildPackage() {
    script.noop()
  }
  
  public void publish() {
    script.noop()
  }
  
  private void preBuild(lvVersion) {
    script.echo "Preparing source for execution with LV $lvVersion..."
    buildSteps.prepareSource(lvVersion)
    script.echo "Applying build configuration to LV $lvVersion..."
    buildSteps.setupLv(lvVersion)
  }
  
  private void postBuild(lvVersion) {
    //Move build output to versioned directory
    script.bat "move \"${buildSteps.BUILT_DIR}\" \"$EXPORT_DIR\\$lvVersion\""
    script.echo "Build for LV Version $lvVersion complete."
  }
}
