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
    buildSteps = this.script.load BUILD_STEPS_LOCATION
  }
  
  public boolean setup() {
    this.script.syncCommonbuild('dynamic-load')
  }
  
  public boolean runUnitTests() {
  }
  
  public boolean build() {
  }
  
  public boolean archive() {
  }
  
  public boolean deploy() {
  }
  
  public boolean publish() {
  }
}
