package ni.vsbuild.notifications

import ni.vsbuild.PipelineResult

abstract class AbstractNotification implements Notification {

   def script

   AbstractNotification(script, notificationInfo) {
      this.script = script
   }

   void notify(PipelineResult pipelineResult) {
      sendNotification(pipelineResult)
   }

   abstract void sendNotification(pipelineResult)

}
