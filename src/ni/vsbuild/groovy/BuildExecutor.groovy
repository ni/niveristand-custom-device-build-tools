package ni.vsbuild.groovy

import ni.vsbuild.AbstractBuildExecutor

class BuildExecutor extends AbstractBuildExecutor {

   private static final String EXPORT_DIR = 'export'

   private String archiveLocation

   public BuildExecutor(script, buildInformation, lvVersion) {
      super(script, buildInformation, lvVersion)
   }

   public void codegen() {
      buildSteps.codegen(lvVersion)
   }
   
   public void build() {
      script.bat "mkdir $EXPORT_DIR"
      
      script.echo "Building for LV Version $lvVersion..."
      preBuild()
      buildSteps.build(lvVersion)
      postBuild()
   }

   public void archive() {
      archiveLocation = "${buildSteps.ARCHIVE_DIR}\\$EXPORT_DIR\\" +
         "${script.env.BRANCH_NAME}\\Build ${script.currentBuild.number}"
      
      script.bat "xcopy \"$EXPORT_DIR\" \"$archiveLocation\" /e /i"
      setArchiveVar(archiveLocation)
   }

   public void buildPackage() {
      script.noop()
   }

   public void publish() {
      script.noop()
   }

   private void postBuild() {
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
