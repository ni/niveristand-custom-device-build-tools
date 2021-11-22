package ni.vsbuild.packages

interface PackageStrategy extends Serializable {

   // This method must return a new list instead of modifying
   // packageCollection directly due to the way the json object
   // stored in the BuildConfiguration is manipulated if using
   // remove/removeAll.
   def filterPackageCollection(packageCollection)

   @NonCPS
   def createNipkgPayloadMap(payloadMap)

}
