package ni.vsbuild.v3.steps

class LvBuildAllStep extends LvBuildStep {

   LvBuildAllStep(script, mapStep, lvVersion) {
      super(script, mapStep, lvVersion)
   }

   void executeBuildStep(String projectPath) {
      script.lvBuildAll(projectPath, lvVersion)
   }
}
