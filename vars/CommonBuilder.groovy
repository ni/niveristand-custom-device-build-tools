class CommonBuilder implements Serializable {
  
  private static final String EXPORT_DIR = 'export'
  private static final String BUILD_STEPS_LOCATION = 'vars/buildSteps.groovy'
  
  private def script
  private String[] lvVersions
  private String sourceVersion
  private String archiveLocation
  private def buildSteps
  
  public CommonBuilder(script, lvVersions, sourceVersion) {
    this.script = script
    this.lvVersions = lvVersions
    this.sourceVersion = sourceVersion
  }
  
  public void loadBuildSteps() {
    buildSteps = script.load BUILD_STEPS_LOCATION
  }
  
  public void setup() {
    // Ensure the VIs for executing scripts are in the workspace
    script.syncCommonbuild('dynamic-load')
    
    script.echo 'Syncing dependencies.'
    buildSteps.syncDependencies()
  }
  
  public void runUnitTests() {
    //Make sure correct dependencies are loaded to run unit tests
    preBuild(sourceVersion)
  }
  
  public void build() {
    script.bat "mkdir $EXPORT_DIR"
    
    lvVersions.toList().each{lvVersion->
      script.echo "Building for LV Version $lvVersion..."
      preBuild(lvVersion)
      buildSteps.build(lvVersion)
      
      //Move build output to versioned directory
      script.bat "move \"${buildSteps.BUILT_DIR}\" \"$EXPORT_DIR\\$lvVersion\""
      script.echo "Build for LV Version $lvVersion complete."
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
    script.echo "CommonBuilder package()"
  }
  
//  public void publish() {
//    script.echo "CommonBuilder publish()"
//  }
  
  private void preBuild(lvVersion) {
    buildSteps.prepareSource(lvVersion)
    buildSteps.setupLv(lvVersion)
  }
}
