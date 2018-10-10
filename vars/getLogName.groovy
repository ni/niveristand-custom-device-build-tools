def call(file) {
   def fileName = file.tokenize("\\").last()
   def logFileName = fileName.split("\\.")[0]
   return logFileName
}
