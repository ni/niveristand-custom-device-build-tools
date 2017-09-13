package ni.vsbuild.nibuild

import ni.vsbuild.AbstractBuildExecutor

class BuildExecutor extends AbstractBuildExecutor {

   public BuildExecutor(script, buildInformation) {
      super(script, buildInformation)
   }

   public void codegen() {
      script.noop()
   }
   
   public void build() {
      script.echo "This stage should call a separate job using nibuild."
      script.noop()
   }

   public void archive() {
      script.noop()
   }

   public void buildPackage() {
      script.echo "This stage should call a seaparate job using nibuild."
      script.noop()
   }

   public void publish() {
      script.noop()
   }
}
