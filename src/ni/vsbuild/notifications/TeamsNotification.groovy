package ni.vsbuild.notifications

class TeamsNotification extends AbstractNotification {

   TeamsNotification(script, notificationInfo) {
      super(script, notificationInfo)
   }

   void sendNotification(pipelineResult) {
      def notificationMessage = NotificationMessage.getSimpleMessage(script, pipelineResult)
      //script.office365ConnectorSend webhookUrl: "${MSTEAMS_HOOK}", message: notificationMessage, status: "${result.name()}"
      script.echo "sending office365Connector to ${script.env.MSTEAMS_HOOK} with message $notificationMessage and status ${result.name()}"
   }
}
