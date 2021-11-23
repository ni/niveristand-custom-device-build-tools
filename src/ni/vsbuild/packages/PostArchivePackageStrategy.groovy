package ni.vsbuild.packages

import ni.vsbuild.Architecture

class PostArchivePackageStrategy implements PackageStrategy {

   def lvVersion

   PostArchivePackageStrategy(lvVersion) {
      this.lvVersion = lvVersion
   }

   def filterPackageCollection(packageCollection) {
      return packageCollection.findAll { it.get('multi_bitness') \
                                          &&  (!(it.get('multi_bitness_versions')) \
                                             || (it.get('multi_bitness_versions').contains(lvVersion.lvRuntimeVersion)))
      }
   }

   def getOutputDirectory(script, outputDir) {
      def component = script.getComponentParts()['repo']
      def archiveLocation = script.env."${component}_DEP_DIR"
      return "$archiveLocation\\${lvVersion.lvRuntimeVersion}"
   }

   def createNipkgPayloadMap(script, payloadMap, outputDir) {
      def newOutputDir = getOutputDirectory(script, outputDir)
      def newMap = [:]
      for (def key : payloadMap.keySet()) {
         Architecture.values().each { newMap[key.replace(outputDir, "$newOutputDir\\$it")] = payloadMap[key] }
      }

      return newMap
   }
}
