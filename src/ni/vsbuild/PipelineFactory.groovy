package ni.vsbuild

class PipelineFactory implements Serializable {
   
   static Pipeline buildPipeline(script, BuildInformation buildInformation) {
      def pipeline
      
      if(buildInformation.packageType == PackageType.NIPM) {
         pipeline = nipm.Pipeline.builder(script, buildInformation).buildPipeline()
      } else {
         script.currentBuild.result = "FAILURE"
         script.error "Build failed: PackageType ${buildInformation.packageType} is not supported."
      }
      
      return pipeline
   }
}
