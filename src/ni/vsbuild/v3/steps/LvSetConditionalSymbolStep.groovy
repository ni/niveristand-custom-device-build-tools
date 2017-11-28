package ni.vsbuild.v3.steps

import ni.vsbuild.v3.BuildConfiguration

class LvSetConditionalSymbolStep extends LvProjectStep {

   def symbol
   def value
   
   LvSetConditionalSymbolStep(script, jsonStep, lvVersion) {
      super(script, jsonStep, lvVersion)
      this.symbol = jsonStep.get('symbol')
      this.value = jsonStep.get('value')
   }
   
   void executeStep(BuildConfiguration configuration) {
      def resolvedProject = resolveProject(configuration)
      def path = resolvedProject.get('path')
      
      script.lvSetConditionalSymbol(path, symbol, value, lvVersion)
   }
}
