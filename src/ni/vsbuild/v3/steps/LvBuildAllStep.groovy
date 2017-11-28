package ni.vsbuild.v3.steps

class LvBuildAllStep extends LvBuildStep {
   
   LvBuildAllStep(script, jsonStep, lvVersion) {
      super(script, jsonStep, lvVersion)
   }
   
   void executeBuildStep(String projectPath) {
      script.lvBuildAll(projectPath, lvVersion)
   }
}
