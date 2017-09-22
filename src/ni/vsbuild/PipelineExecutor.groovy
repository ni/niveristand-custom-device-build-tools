package ni.vsbuild

class PipelineExecutor implements Serializable {

   static void execute(script, BuildInformation buildInformation) {
      buildInformation.printInformation(script)
      
      def pipeline
      def builders = [:]
      
      if(buildInformation.packageType == PackageType.NIPM) {
         pipeline = nipm.Pipeline.builder(script, buildInformation).buildPipeline()
      } else {
         script.currentBuild.result = "FAILURE"
         script.error "Build failed: PackageType ${buildInformation.packageType} is not supported."
      }
      
      for (String version in buildInformation.lvVersions) {
         def lvVersion = version // need to bind the variable before the closure - can't do 'for (lvVersion in lvVersions)
         builders[lvVersion] = {
            pipeline.execute(lvVersion)
         }
      }
      
      script.stage ('CI Build') {
         script.parallel builders
      }
   }
}
