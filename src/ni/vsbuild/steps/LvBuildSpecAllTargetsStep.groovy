package ni.vsbuild.steps

class LvBuildSpecAllTargetsStep extends LvBuildStep {

   def spec

   LvBuildSpecAllTargetsStep(script, mapStep, lvVersion) {
      super(script, mapStep, lvVersion)
      this.spec = mapStep.get('build_spec')
   }

   void executeBuildStep(String projectPath) {
      script.lvBuildSpecAllTargets(projectPath, spec, lvVersion)
   }
}
