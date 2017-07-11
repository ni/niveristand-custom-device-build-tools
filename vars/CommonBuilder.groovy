class CommonBuilder implements Serializable {
  
  private static final String EXPORT_DIR = 'export'
  private static final String BUILD_STEPS_LOCATION = 'vars/buildSteps.groovy'
  
  private String nodeLabel
  private String[] lvVersions
  private String sourceVersion
  private String archiveLocation
  private def buildSteps
  
  public CommonBuilder(nodeLabel, lvVersions, sourceVersion) {
    this.nodeLabel = nodeLabel
    this.lvVersions = lvVersions
    this.sourceVersion = sourceVersion
    echo "CommonBuilder initialized with nodeLabel ${this.nodeLabel}, lvVersions ${this.lvVersions}, sourceVersion ${this.sourceVersion}"
  }
  
  public boolean loadBuildSteps() {
    buildSteps = load BUILD_STEPS_LOCATION
  }
  
  public boolean setup() {
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
