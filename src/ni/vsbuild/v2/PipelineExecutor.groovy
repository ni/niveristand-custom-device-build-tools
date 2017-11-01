package ni.vsbuild.v2

class PipelineExecutor implements Serializable {

   static void execute(script) {
      script.node('dcafbuild01') {
         script.stage('checkout') {
            script.deleteDir()
            script.echo 'Attempting to get source from repo.'
            script.timeout(time: 5, unit: 'MINUTES'){
               script.checkout(script.scm)
            }
         }
         script.stage('setup') {
            script.cloneCommonbuild()
            script.bat "pip --version"
            script.bat "commonbuild\\scripts\\buildSetup.bat"
         }
         script.stage('read config') {
            def configuration = BuildConfiguration.load(script, 'output.txt')
            configuration.printInformation(script)
         }
      }
      
      //def configuration = BuildConfiguration.load(script, jsonFile)
      //configuration.printInformation(script)

      //PipelineFactory.buildPipeline(script, buildInformation).execute()
   }
}
