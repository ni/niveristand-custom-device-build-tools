def call(retryAttempts = 3) {
   def firstAttempt = true

   script.retry(retryAttempts) {
      script.echo 'Attempting to delete workspace'
      if (!firstAttempt) {
         script.sleep time: 5 unit: SECONDS
      } else {
         firstAttempt = false
      }

      script.deleteDir()
   }
}
