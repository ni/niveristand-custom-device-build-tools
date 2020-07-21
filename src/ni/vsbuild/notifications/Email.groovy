package ni.vsbuild.notifications

class Email extends AbstractNotification {

   def recipients

   Email(script, notificationInfo) {
      super(script, notificationInfo)
      this.payloadDir = packageInfo.get('recipients')
   }

   void sendNotification(message) {
      return
   }
}
