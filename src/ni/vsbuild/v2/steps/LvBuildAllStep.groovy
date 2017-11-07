package ni.vsbuild.v2.steps

import ni.vsbuild.v2.BuildConfiguration

class LvBuildAllStep extends LvBuildStep {
   
   LvBuildAllStep(script, jsonStep, lvVersion) {
      super(script, jsonStep, lvVersion)
   }
   
   //void executeBuildStep(String projectPath) {
      //script.lvBuildAll(projectPath, lvVersion)
   //}
   
   void executeBuildStep(projectEntry) {
      def projectPath = projectEntry.getString('path')
      script.lvBuildAll(projectPath, lvVersion)
   }
}
