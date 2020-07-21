package ni.vsbuild

enum PipelineResult {
   SUCCESS('succeeded'),
   FIXED('fixed'),
   NEW_FAILURE('failed'),
   EXISTING_FAILURE('still failing'),
   UNSTABLE('unstable'),
   ABORTED('aborted')

   private final String value

   PipelineResult(String value) {
      this.value = value
   }

   @NonCPS
   @Override
   public String toString() {
      return value
   }
}
