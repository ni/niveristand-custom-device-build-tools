package ni.vsbuild.steps

class LvBuildSpecStep extends LvBuildStep {

   def target
   def spec

   LvBuildSpecStep(script, mapStep, lvVersion) {
      super(script, mapStep, lvVersion)
      this.target = mapStep.get('target')
      this.spec = mapStep.get('build_spec')
   }

   void executeBuildStep(String projectPath) {
      script.lvBuildSpec(projectPath, target, spec, lvVersion)
   }
}
