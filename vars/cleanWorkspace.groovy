def call(retryAttempts = 3) {
   def firstAttempt = true

   retry(retryAttempts) {
      echo 'Attempting to delete workspace'
      if (!firstAttempt) {
         sleep time: 5, unit: SECONDS
      } else {
         firstAttempt = false
      }

      deleteDir()
   }
}
