package ni.vsbuild

abstract class AbstractBuildExecutor implements BuildExecutor {

   protected final def script
   protected final BuildInformation buildInformation
   protected final String lvVersion
   protected def buildSteps

   public AbstractBuildExecutor(script, BuildInformation buildInformation, String lvVersion) {
      this.script = script
      this.buildInformation = buildInformation
      this.lvVersion = lvVersion
   }

   public void loadBuildSteps(buildStepsLocation) {
      checkout()
      
      def component = script.getComponentParts()['repo']
      script.echo "Loading build steps from $component/$buildStepsLocation"
      buildSteps = script.load buildStepsLocation
   }

   public void setup() {
      script.cloneCommonbuild()
   }

   public void runUnitTests() {
      //Make sure correct dependencies are loaded to run unit tests
      preBuild()
      script.noop()
   }

   public abstract void codegen()

   public abstract void build()

   public abstract void archive()

   public abstract void buildPackage()

   public abstract void publish()
   
   protected abstract void getCheckoutStageName()

   protected void preBuild() {
      script.echo "Preparing source for execution with LV $lvVersion..."
      buildSteps.prepareSource(lvVersion)
      script.echo "Applying build configuration to LV $lvVersion..."
      buildSteps.setupLv(lvVersion)
   }
   
   private void checkout() {
      script.stage(getCheckoutStageName()) {
         script.deleteDir()
         script.echo 'Attempting to get source from repo.'
         script.timeout(time: 5, unit: 'MINUTES'){
            script.checkout(script.scm)
         }
      }
   }
}
