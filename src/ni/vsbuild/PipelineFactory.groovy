package ni.vsbuild

class PipelineFactory implements Serializable {
   
   static Pipeline buildPipeline(script, BuildInformation buildInformation) {
      
      if(buildInformation.packageType == PackageType.NIPM) {
         return nipm.PipelineFactory(script, buildInformation).buildPipeline()
      }
      
      script.currentBuild.result = "FAILURE"
      script.error "Build failed: PackageType ${buildInformation.packageType} is not supported."
   }
}
