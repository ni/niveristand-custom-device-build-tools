package ni.vsbuild.steps

import ni.vsbuild.BuildConfiguration

class LvSetConditionalSymbolStep extends LvProjectStep {

   def symbol
   def value

   LvSetConditionalSymbolStep(script, mapStep, lvVersion) {
      super(script, mapStep, lvVersion)
      this.symbol = mapStep.get('symbol')
      this.value = mapStep.get('value')
   }

   void executeStep(BuildConfiguration configuration) {
      def resolvedProject = resolveProject(configuration)
      def path = resolvedProject.get('path')

      script.lvSetConditionalSymbol(path, symbol, value, lvVersion)
   }
}
