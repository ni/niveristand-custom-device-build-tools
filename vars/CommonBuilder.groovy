class CommonBuilder implements Serializable {
  
  private static final String EXPORT_DIR = 'export'
  private static final String BUILD_STEPS_LOCATION = 'vars/buildSteps.groovy'
  
  private String[] lvVersions
  private String sourceVersion
  private String archiveLocation
  private def buildSteps
  private def script
  
  public CommonBuilder(script, lvVersions, sourceVersion) {
    this.script = script
    this.lvVersions = lvVersions
    this.sourceVersion = sourceVersion
  }
  
  public def loadBuildSteps() {
    this.buildSteps = this.script.load BUILD_STEPS_LOCATION
    return this.buildSteps
  }
  
  public boolean setup() {
    this.script.syncCommonbuild('dynamic-load')
    
    this.script.echo 'Syncing dependencies.'
    this.buildSteps.syncDependencies()
  }
  
  public boolean runUnitTests() {
    this.script.echo 'Commonbuild run unit tests'
    this.preBuild(this.sourceVersion)
  }
  
  public boolean build() {
  }
  
  public boolean archive() {
  }
  
  public boolean deploy() {
  }
  
  public boolean publish() {
  }
  
  private def preBuild(lvVersion) {
    this.script.echo "Commonbuild preBuild using LV version $lvVersion"
    this.buildSteps.prepareSource(lvVersion)
    this.buildSteps.setupLv(lvVersion)
  }
}
