package ni.vsbuild.v2.steps

import ni.vsbuild.v2.BuildConfiguration

class LvBuildSpecAllTargetsStep extends LvBuildStep {

   def spec
   
   LvBuildSpecAllTargetsStep(script, jsonStep, lvVersion) {
      super(script, jsonStep, lvVersion)
      this.spec = jsonStep.getString('build_spec')
   }
   
   void executeBuildStep(String projectPath) {
      script.lvBuildSpecAllTargets(projectPath, spec, lvVersion)
   }
}
