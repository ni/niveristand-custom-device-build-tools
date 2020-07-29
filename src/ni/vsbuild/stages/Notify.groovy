package ni.vsbuild.stages

import ni.vsbuild.PipelineResult
import ni.vsbuild.notifications.Notification
import ni.vsbuild.notifications.NotificationFactory

class Notify implements Stage {

   private static final String STAGE_NAME = 'Notify'
   
   // Default to match "master" exactly and "release" followed by a / or -
   // followed by a numeric version number
   // https://regex101.com/r/dGkJ3o/1
   private static final String DEFAULT_BRANCHES = "(^master\$|^release[-/]+[0-9.]+)"

   def script
   def nodeLabel
   def notificationInfo
   PipelineResult pipelineResult

   Notify(script, nodeLabel, notificationInfo, pipelineResult) {
      this.script = script
      this.nodeLabel = nodeLabel
      this.notificationInfo = notificationInfo
      this.pipelineResult = pipelineResult
   }

   void execute() {
      // Only notify if current branch matches the regex used for determining whether
      // to send notification
      def notificationBranches = notificationInfo.get('branch_pattern') ?: DEFAULT_BRANCHES
      if (!script.env.BRANCH_NAME.matches(notificationBranches)) {
         return
      }

      script.node(nodeLabel) {
         script.stage(STAGE_NAME) {
            def notificationInfoCollection = []

            // Developers can specify a single notification [Notification]
            // or a collection of notifications [[Notification]].
            // Test the notification information parameter and iterate as needed.
            if (notificationInfo instanceof Collection) {
               notificationInfoCollection = notificationInfo
            }
            else {
               notificationInfoCollection.add(notificationInfo)
            }

            for (def notificationInfoItem : notificationInfoCollection) {
               Notification notification = NotificationFactory.createNotification(script, notificationInfoItem)
               notification.notify(pipelineResult)
            }
         }
      }
   }
}
