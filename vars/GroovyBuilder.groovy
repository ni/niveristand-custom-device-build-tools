class GroovyBuilder extends CommonBuilder {

  private static final String EXPORT_DIR = 'export'

  private String archiveLocation
  private def buildSteps

  public GroovyBuilder(script, buildInformation) {
    super(script, buildInformation)
  }

  public void loadBuildSteps(buildStepsLocation) {
    def component = script.getComponentParts()['repo']
    script.echo "Loading build steps from $component/$buildStepsLocation"
    buildSteps = script.load buildStepsLocation
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

  protected void builderSetup() {
    script.echo 'Syncing dependencies.'
    buildSteps.syncDependencies()
  }

  protected void preBuild(lvVersion) {
    script.echo "Preparing source for execution with LV $lvVersion..."
    buildSteps.prepareSource(lvVersion)
    script.echo "Applying build configuration to LV $lvVersion..."
	buildSteps.setupLv(lvVersion)
  }

  protected void postBuild(lvVersion) {
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
