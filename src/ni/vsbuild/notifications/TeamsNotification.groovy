package ni.vsbuild.notifications

class TeamsNotification extends AbstractNotification {

   TeamsNotification(script, notificationInfo) {
      super(script, notificationInfo)
   }

   void sendNotification(pipelineResult) {
      def notificationMessage = NotificationMessage.getSimpleMessage(script, pipelineResult)
      script.office365ConnectorSend webhookUrl: "${script.env.MSTEAMS_HOOK}", message: notificationMessage, status: "${pipelineResult.name()}"
   }
}
