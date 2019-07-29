def call() {
   def partMap = [:]
   def tokens = env.JOB_NAME.tokenize("/")
   partMap['branch'] = tokens[-1]
   partMap['repo'] = tokens[-2]
   if(tokens[-3]) {
      partMap['organization'] = tokens[-3]
   }

   return partMap
}
