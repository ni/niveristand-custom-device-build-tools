package ni.vsbuild.v2

class PipelineExecutor implements Serializable {

   static void execute(script) {
      node('dcafbuild01'){
         stage('checkout') {
         deleteDir()
         echo 'Attempting to get source from repo.'
         timeout(time: 5, unit: 'MINUTES'){
            checkout(scm)
            }
         }
         stage('read config') {
            def configuration = BuildConfiguration.load(this, 'output.txt')
            configuration.printInformation(this)
         }
      }
      
      //def configuration = BuildConfiguration.load(script, jsonFile)
      //configuration.printInformation(script)

      //PipelineFactory.buildPipeline(script, buildInformation).execute()
   }
}
