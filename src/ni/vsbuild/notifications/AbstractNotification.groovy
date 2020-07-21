package ni.vsbuild.notifications

import ni.vsbuild.PipelineResult

abstract class AbstractNotification implements Notification {

   def script
   def type

   AbstractNotification(script, notificationInfo) {
      this.script = script
      this.type = notificationInfo.get('type')
   }

   void notify(PipelineResult pipelineResult) {
      sendNotification(pipelineResult)
   }

   abstract void sendNotification(pipelineResult)

}
