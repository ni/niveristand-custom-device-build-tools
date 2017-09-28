package ni.vsbuild

class PipelineFactory implements Serializable {
   
   static Pipeline buildPipeline(script, BuildInformation buildInformation) {
      
      def pipeline
      
      if(buildInformation.packageType == PackageType.NIPM) {
         pipeline = nipm.PipelineFactory(script, buildInformation).buildPipeline()
         return pipeline
      }
      
      script.currentBuild.result = "FAILURE"
      script.error "Build failed: PackageType ${buildInformation.packageType} is not supported."
   }
}
