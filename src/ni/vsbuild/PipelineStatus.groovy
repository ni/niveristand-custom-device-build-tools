package ni.vsbuild

class PipelineStatus implements Serializable {

   // Taken from https://github.com/jenkinsci/jenkins/blob/master/core/src/main/java/hudson/model/Result.java
   private static final String SUCCESS_STRING = 'SUCCESS'
   private static final String FAILURE_STRING = 'FAILURE'
   private static final String UNSTABLE_STRING = 'UNSTABLE'
   private static final String NOT_BUILT_STRING = 'NOT_BUILT'
   private static final String ABORTED_STRING = 'ABORTED'

   public static PipelineResult getResult(def script) {
      // Modified from https://jenkins.io/doc/pipeline/tour/running-multiple-steps/#finishing-up
      // Default to success if no result exists yet
      def currentResult = script.currentBuild.result ?: SUCCESS_STRING
      def previousResult = script.currentBuild.previousBuild?.result
      def currentFailure = (currentResult == FAILURE_STRING || currentResult == NOT_BUILT_STRING)

      if (currentResult == UNSTABLE_STRING) {
         return PipelineResult.UNSTABLE
      }

      if (currentResult == ABORTED_STRING) {
         return PipelineResult.ABORTED
      }

      if (previousResult) {
         PipelineResult result

         if (currentResult == previousResult) {
            // Build is still good or still broken
            result = currentFailure ? PipelineResult.EXISTING_FAILURE : PipelineResult.SUCCESS
         }
         else {
            // Build is newly fixed or newly broken
            result = currentFailure ? PipelineResult.NEW_FAILURE : PipelineResult.FIXED
         }

         return result
      }

      // First build is either good or new failure
      return currentFailure ? PipelineResult.NEW_FAILURE : PipelineResult.SUCCESS
   }
}
