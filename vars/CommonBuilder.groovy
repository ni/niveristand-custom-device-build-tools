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
  
  public def loadBuildSteps() {
    this.buildSteps = this.script.load BUILD_STEPS_LOCATION
    return this.buildSteps
  }
  
  public def setup() {
    // Ensure the VIs for executing scripts are in the workspace
    this.script.syncCommonbuild('dynamic-load')
    
    this.script.echo 'Syncing dependencies.'
    this.buildSteps.syncDependencies()
  }
  
  public def runUnitTests() {
    //Make sure correct dependencies are loaded to run unit tests
    this.preBuild(this.sourceVersion)
  }
  
  public def build() {
    this.script.bat "mkdir $EXPORT_DIR"
    
    lvVersions.toList().each{lvVersion->
      this.script.echo "Building for LV Version $lvVersion..."
      this.preBuild(lvVersion)
      this.buildSteps.build(lvVersion)
      
      //Move build output to versioned directory
      this.script.bat "move \"${this.buildSteps.BUILT_DIR}\" \"$EXPORT_DIR\\$lvVersion\""
      this.script.echo "Build for LV Version $lvVersion complete."
    }
  }
  
  public def archive() {
    //this.archiveLocation = this.script.archiveBuild(EXPORT_DIR, this.buildSteps.ARCHIVE_DIR)
    this.archiveLocation = "${this.buildSteps.ARCHIVE_DIR}\\$EXPORT_DIR"
  
    //don't do this delete with the actual archive
    //this is for testing purposes only
    if(this.script.fileExists(this.archiveLocation)){
      this.script.bat "rmdir \"${this.archiveLocation}\" /s /q"
    }
  
    this.script.bat "xcopy \"$EXPORT_DIR\" \"${this.archiveLocation}\" /e /i"
  }
  
  public def deploy() {
  }
  
  public def publish() {
  }
  
  private def preBuild(lvVersion) {
    this.buildSteps.prepareSource(lvVersion)
    this.buildSteps.setupLv(lvVersion)
  }
}
