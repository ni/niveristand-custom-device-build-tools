package ni.vsbuild.stages

import ni.vsbuild.BuildConfiguration

class Archive extends AbstractStage {

   private static final String MANIFEST_ARCHIVE_DIR = 'installer'

   private String archiveLocation
   private String manifestFile

   Archive(script, configuration, lvVersion, manifestFile) {
      super(script, 'Archive', configuration, lvVersion)

      this.manifestFile = manifestFile
   }

   void executeStage() {
      setArchiveLocation()

      script.echo "Archiving build to $archiveLocation"
      def buildOutputDir = configuration.archive.get('build_output_dir')

      if(script.fileExists(BuildConfiguration.STAGING_DIR)) {
         buildOutputDir = BuildConfiguration.STAGING_DIR
      }

      def versionedArchive = "$archiveLocation\\$lvVersion"
      script.copyFiles(buildOutputDir, versionedArchive)

      archiveManifest(versionedArchive)

      setArchiveVar()
   }

   // Builds a string of the form <archiveLocation>\\export\\<branch>\\<build_number>
   private void setArchiveLocation() {
      def organization = script.getComponentParts()['organization']

      // Organization may not exist for multibranch pipelines not using
      // the GitHub Branch Source Plugin
      if(!organization) {
         organization = ''
      }
      else {
         organization = "$organization\\"
      }

      archiveLocation = "${configuration.archive.get('archive_location')}\\" +
         "$organization" +
         "export\\${script.env.BRANCH_NAME}\\" +
         "Build ${script.currentBuild.number}"
   }

   // Set an env var that points to the archive so dependents can find it
   private void setArchiveVar() {
      def component = script.getComponentParts()['repo']
      def depDir = "${component}_DEP_DIR"
      script.env."$depDir" = archiveLocation
   }
   
   private void archiveManifest(String versionedArchive) {
      def splitIndex = manifestFile.lastIndexOf('/')
      def manifestFileName = manifestFile.substring(splitIndex + 1)
      
      def versionedInstallerDir = "$versionedArchive\\$MANIFEST_ARCHIVE_DIR"

      if(!script.fileExists("$versionedInstallerDir\\$manifestFileName")) {
         def manifestDirectory = manifestFile.take(splitIndex)
         script.copyFiles(manifestDirectory, versionedInstallerDir, [files: manifestFileName])
      }
   }
}
