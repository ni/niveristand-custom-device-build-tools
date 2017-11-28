package ni.vsbuild.v3.steps

class LvBuildSpecStep extends LvBuildStep {

   def target
   def spec
   
   LvBuildSpecStep(script, jsonStep, lvVersion) {
      super(script, jsonStep, lvVersion)
      this.target = jsonStep.getString('target')
      this.spec = jsonStep.getString('build_spec')
   }
   
   void executeBuildStep(String projectPath) {
      script.lvBuildSpec(projectPath, target, spec, lvVersion)
   }
}
