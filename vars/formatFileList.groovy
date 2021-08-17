def call(files) {
   def fileList = files.tokenize(' ')
   def formattedFiles = fileList.collect{file -> "\"$file\""}.join(' ')
   return formattedFiles
}