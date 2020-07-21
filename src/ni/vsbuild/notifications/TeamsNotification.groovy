package ni.vsbuild.notifications

class TeamsNotification extends AbstractNotification {

   private static final String GREEN = '00FF00'
   private static final String RED = 'FF0000'

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
      def successfulResult = (pipelineResult == PipelineResult.SUCCESS || pipelineResult == PipelineResult.FIXED)
      def notificationColor = successfulResult ? TeamsNotification.GREEN : TeamsNotification.RED
      return notificationColor
   }
}
