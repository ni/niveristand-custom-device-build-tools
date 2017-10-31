package ni.vsbuild.v2

abstract class AbstractStep implements Step {
   
   public final String name
   
   AbstractStep(String name) {
      this.name = name
   }
   
   execute() {
      this.runStep()
   }
   
   protected abstract void runStep()
}
