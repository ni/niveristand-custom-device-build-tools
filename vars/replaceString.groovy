def call() {
   def lvVersion = '2017'
   def currentVersion = lvVersion as Integer
   def previousVersion = currentVersion - 1
   def oldAssemblyVersion = "0.0.0.0-${previousVersion}.9.9.9"
   def newAssemblyVersion = "${currentVersion}.0.0.0"
   def testString = "oldVersion=\"0.0.0.0-2015.1.0.0\" newVersion=\"2016.0.0.0\""
   echo "$testString"
   def changedString = testString.replaceAll("(oldVersion=\")[^\"]+","\$1$oldAssemblyVersion")
   changedString = changedString.replaceAll("(newVersion=\")[^\"]+","\$1$newAssemblyVersion")
   echo "Changed to $changedString."
}
