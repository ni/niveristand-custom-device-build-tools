package ni.vsbuild.nipm

import ni.vsbuild.BuildInformation
import ni.vsbuild.stages.*

class Pipeline implements Serializable {

   def script
   def prebuildStages = []
   def buildStages = []
   BuildInformation buildInformation

   static builder(script, BuildInformation buildInformation) {
      return new Builder(script, buildInformation)
   }

   static class Builder implements Serializable {

      def script
      BuildInformation buildInformation

      Builder(def script, BuildInformation buildInformation) {
         this.script = script
         this.buildInformation = buildInformation
      }
      
      def buildPipeline() {
         def pipelineBuilder = BuilderFactory.createBuilder(script, buildInformation)
         pipelineBuilder.build()
         
         return new Pipeline(pipelineBuilder)
      }
   }

   private Pipeline(builder) {
      this.script = builder.script
      this.prebuildStages = builder.prebuildStages
      this.buildStages = builder.buildStages
      this.buildInformation = builder.buildInformation
   }

   void execute(lvVersion) {

      // build dependencies before starting this pipeline
      script.buildDependencies(buildInformation)
      
      script.node(buildInformation.nodeLabel) {
         
         def executor
         
         executeStages(prebuildStages, executor)
         
         // This load must happen after the checkout stage, but before any
         // stage that requires the build steps to be loaded
         executor = buildInformation.createExecutor(script, lvVersion)
         
         executeStages(buildStages, executor)
      }
   }
   
   private void executeStages(stages, executor) {
      for (Stage stage : stages) {
         try {
            stage.execute(executor)
         } catch (err) {
            script.currentBuild.result = "FAILURE"
            script.error "Build failed: ${err.getMessage()}"
         }
      }
   }
}
