package ni.vsbuild.v2.steps

import ni.vsbuild.v2.BuildConfiguration

class LvBuildSpecStep extends LvBuildStep {

   def target
   def spec
   
   LvBuildSpecStep(script, jsonStep, lvVersion) {
      super(script, jsonStep, lvVersion)
      this.target = jsonStep.getString('target')
      this.spec = jsonStep.getString('build_spec')
   }
   
   //void executeBuildStep(String projectPath) {
      //script.lvBuildSpec(projectPath, target, spec, lvVersion)
   //}
   
   void executeBuildStep(projectEntry) {
      def projectPath = projectEntry.getString('path')
      script.lvBuildSpec(projectPath, target, spec, lvVersion)
   }
   
   protected def resolveTargets(BuildConfiguration configuration) {
      def targets = []
      
      if(project == all) {
         
      }
      
      return [target]
   }
}
