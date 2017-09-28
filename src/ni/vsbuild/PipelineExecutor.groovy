package ni.vsbuild

class PipelineExecutor implements Serializable {

   static void execute(script, BuildInformation buildInformation) {
      buildInformation.printInformation(script)
      
      def builders = [:]
      def pipeline = PipelineFactory.buildPipeline(script, buildInformation)
      
      for (String version in buildInformation.lvVersions) {
         def lvVersion = version // need to bind the variable before the closure - can't do 'for (lvVersion in lvVersions)
         builders[lvVersion] = {
            pipeline.execute(lvVersion)
         }
      }
      
      script.parallel builders
   }
}
