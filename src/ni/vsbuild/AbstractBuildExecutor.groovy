package ni.vsbuild

abstract class AbstractBuildExecutor implements BuildExecutor, Serializable {

   protected final def script
   protected final BuildInformation buildInformation
   protected def buildSteps

   public AbstractBuildExecutor(script, BuildInformation buildInformation) {
      this.script = script
      this.buildInformation = buildInformation
   }

   public void loadBuildSteps(buildStepsLocation) {
      def component = script.getComponentParts()['repo']
      script.echo "Loading build steps from $component/$buildStepsLocation"
      buildSteps = script.load buildStepsLocation
   }

   public void setup() {
      script.syncCommonbuild()
   }

   public void runUnitTests() {
      //Make sure correct dependencies are loaded to run unit tests
      preBuild(buildInformation.sourceVersion)
      script.noop()
   }

   public abstract void codegen()

   public abstract void build()

   public abstract void archive()

   public abstract void buildPackage()

   public abstract void publish()

   protected void preBuild(lvVersion) {
      script.echo "Preparing source for execution with LV $lvVersion..."
      buildSteps.prepareSource(lvVersion)
      script.echo "Applying build configuration to LV $lvVersion..."
      buildSteps.setupLv(lvVersion)
   }
}
