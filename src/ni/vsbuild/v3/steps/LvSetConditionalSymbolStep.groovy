package ni.vsbuild.v2.steps

import ni.vsbuild.v2.BuildConfiguration

class LvSetConditionalSymbolStep extends LvProjectStep {

   def symbol
   def value
   
   LvSetConditionalSymbolStep(script, jsonStep, lvVersion) {
      super(script, jsonStep, lvVersion)
      this.symbol = jsonStep.getString('symbol')
      this.value = jsonStep.getString('value')
   }
   
   void executeStep(BuildConfiguration configuration) {
      def resolvedProject = resolveProject(configuration)
      def path = resolvedProject.getString('path')
      
      script.lvSetConditionalSymbol(path, symbol, value, lvVersion)
   }
}
