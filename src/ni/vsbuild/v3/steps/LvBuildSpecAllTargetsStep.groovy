package ni.vsbuild.v3.steps

class LvBuildSpecAllTargetsStep extends LvBuildStep {

   def spec
   
   LvBuildSpecAllTargetsStep(script, jsonStep, lvVersion) {
      super(script, jsonStep, lvVersion)
      this.spec = jsonStep.get('build_spec')
   }
   
   void executeBuildStep(String projectPath) {
      script.lvBuildSpecAllTargets(projectPath, spec, lvVersion)
   }
}
