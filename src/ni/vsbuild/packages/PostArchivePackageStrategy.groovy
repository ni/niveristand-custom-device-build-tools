package ni.vsbuild.packages

import ni.vsbuild.Architecture

class PostArchivePackageStrategy implements PackageStrategy {

   def lvVersion

   PostArchivePackageStrategy(lvVersion) {
      this.lvVersion = lvVersion
   }

   // Returns all packages with a configuration where
   // multi_bitness is defined AND
   // the current LabVIEW version matches one of the versions
   // specified for multi-bitness packaging.
   // If no versions are specified, include the package with the
   // assumption that multi_bitness exists for ALL versions.
   def filterPackageCollection(packageCollection) {
      return packageCollection.findAll { it.get('multi_bitness') \
                                          &&  (!(it.get('multi_bitness_versions')) \
                                             || (it.get('multi_bitness_versions').contains(lvVersion.lvRuntimeVersion)))
      }
   }

   def getOutputDirectory(script, outputDir) {
      def baseOutputDirectory = getBaseOutputDirectory(script)
      return "$baseOutputDirectory\\${AbstractPackage.INSTALLER_DIRECTORY}"
   }

   def createNipkgPayloadMap(script, payloadMap, outputDir) {
      def newOutputDir = getBaseOutputDirectory(script)
      def newMap = [:]
      for (def key : payloadMap.keySet()) {
         Architecture.values().each { newMap[key.replace(outputDir, "$newOutputDir\\$it")] = payloadMap[key] }
      }

      return newMap
   }

   private def getBaseOutputDirectory(script) {
      def component = script.getComponentParts()['repo']
      def archiveLocation = script.env."${component}_DEP_DIR"
      return "$archiveLocation\\${lvVersion.lvRuntimeVersion}"
   }
}
