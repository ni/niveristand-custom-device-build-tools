package ni.vsbuild.v2.stages

class Archive extends AbstractStage {

   private String archiveLocation
   
   Archive(script, configuration, lvVersion) {
      super(script, 'Archive', configuration, lvVersion)
   }
   
   void executeStage() {
      buildArchiveDir()
      
      script.echo "Archiving build to $archiveLocation"
      def buildOutputDir = configuration.archive.getString('build_output_dir')
      script.bat "xcopy \"$buildOutputDir\" \"$archiveLocation\" /e /i"
      
      setArchiveVar()
   }
   
   // Builds a string of the form <archiveLocation>\\export\\<branch>\\<build_number>
   private void buildArchiveDir() {
      archiveLocation = configuration.archive.getString('archive_location') +
                "\\export\\${script.env.BRANCH_NAME}\\$lvVersion\\" +
                "Build ${script.currentBuild.number}"
   }
   
   // Set an env var that points to the archive so dependents can find it
   private void setArchiveVar() {
      def component = script.getComponentParts()['repo']
      def depDir = "${component}_DEP_DIR"
      script.env[depDir] = archiveLocation
   }
}
