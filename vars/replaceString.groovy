def call() {
   def lvVersion = '2017'
   def versionAsInteger = lvVersion as Integer
   def oldMax = versionAsInteger - 1
   def testString = "oldVersion=\"0.0.0.0-2015.1.0.0\" newVersion=\"2016.0.0.0\""
   echo "$testString"
   def changedString = testString.replaceAll("(oldVersion=\")[^\"]+","\$10.0.0.0-${oldMax}.9.9.9")
   changedString = changedString.replaceAll("(newVersion=\")[^\"]+","\$1${versionAsInteger}.0.0.0")
   echo "Changed to $changedString."
}
