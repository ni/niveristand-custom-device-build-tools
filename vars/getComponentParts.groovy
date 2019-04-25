def call() {
   def partMap = [:]
   def tokens = env.JOB_NAME.tokenize("/")
   def tokenCount = tokens.size()

   def index = -1
   def parts = ['branch', 'repo', 'organization']

   for(i = 0; i < tokenCount; i++) {
      if(parts[i]) {
         partMap[parts[i]] = tokens[index]
      }
      index--
   }

   return partMap
}
