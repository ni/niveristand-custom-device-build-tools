package ni.vsbuild.notifications

import ni.vsbuild.PipelineResult

class NotificationFactory implements Serializable {

   static Notification createNotification(script, notificationInfo) {
      def type = notificationInfo.get('type')

      if (type == 'teams') {
         return new TeamsNotification(script, notificationInfo)
      }

      script.failBuild("\'$type\' is an invalid notification type.")
   }
}
