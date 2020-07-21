package ni.vsbuild.stages

import ni.vsbuild.PipelineResult
import ni.vsbuild.notifications.Notification
import ni.vsbuild.notifications.NotificationFactory

class Notify implements Stage {

   private static final String STAGE_NAME = 'Notify'

   def script
   def configuration
   PipelineResult pipelineResult

   Notify(script, configuration, pipelineResult) {
      this.script = script
      this.configuration = configuration
      this.pipelineResult = pipelineResult
   }

   void execute() {
      script.stage(STAGE_NAME) {
         def notificationInfoCollection = []

         // Developers can specify a single notification [Notification]
         // or a collection of notifications [[Notification]].
         // Test the notification information parameter and iterate as needed.
         if (configuration.notificationInfo instanceof Collection) {
            notificationInfoCollection = configuration.notificationInfo
         }
         else {
            notificationInfoCollection.add(configuration.notificationInfo)
         }

         for (def notificationInfo : notificationInfoCollection) {
            Notification notification = NotificationFactory.createNotification(script, notificationInfo)
            notification.notify(pipelineResult)
         }
      }
   }
}
