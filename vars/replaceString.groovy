def call() {
   def lvVersion = '2017'
   def versionNumber = lvVersion as Integer
   def oldMax = versionNumber - 1
   def testString = "oldVersion=\"0.0.0.0-2015.1.0.0\" newVersion=\"2016.0.0.0\""
   echo "$testString"
   def changedString = testString.replaceAll("(oldVersion=\")[^\"]+","\$10.0.0.0-${oldMax}.9.9.9")
   echo "Changed to $changedString."
}
