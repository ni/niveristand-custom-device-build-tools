def call() {
   def testString = "oldVersion=\"0.0.0.0-2015.1.0.0\" newVersion=\"2016.0.0.0\""
   echo "$testString"
   def changedString = testString.replaceAll("(oldVersion=\")[^\"]+","\$1")
   echo "Changed to $changedString."
}
