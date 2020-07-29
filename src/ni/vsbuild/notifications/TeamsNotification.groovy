package ni.vsbuild.notifications

import ni.vsbuild.PipelineResult

class TeamsNotification extends AbstractNotification {

   private static final String GRAY = '808080'
   private static final String GREEN = '008000'
   private static final String RED = 'FF0000'
   private static final String YELLOW = 'FFFF00'

   TeamsNotification(script, notificationInfo) {
      super(script, notificationInfo)
   }

   void sendNotification(pipelineResult) {
      def notificationMessage = "Build ${pipelineResult.toString()}."
      script.office365ConnectorSend webhookUrl: "${script.env.MSTEAMS_HOOK}",
         message: notificationMessage,
         status: "${pipelineResult.name()}",
         color: getNotificationColor(pipelineResult)
   }

   private String getNotificationColor(pipelineResult) {
      if (pipelineResult == PipelineResult.ABORTED) {
         return TeamsNotification.GRAY
      }

      if (pipelineResult == PipelineResult.UNSTABLE) {
         return TeamsNotification.YELLOW
      }

      def successfulResult = (pipelineResult == PipelineResult.SUCCESS || pipelineResult == PipelineResult.FIXED)
      def notificationColor = successfulResult ? TeamsNotification.GREEN : TeamsNotification.RED
      return notificationColor
   }
}
