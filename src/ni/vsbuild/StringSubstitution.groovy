package ni.vsbuild

class StringSubstitution implements Serializable {

   public static String replaceStrings(text, lvVersion, additionalReplacements = [:]) {
      println("Replacing strings")
      def replacements = [
            "labview_version": lvVersion,
            "veristand_version": lvVersion,
            "labview_short_version": lvVersion[-2..-1],
      ]
      println("Built initial replacements")
      replacements << additionalReplacements
      println("Added additional replacements")

      def updatedText = text
      replacements.each { expression, value ->
         updatedText = updatedText.replaceAll("\\{$expression\\}", value)
      }

      println("Replaced all strings")
      return updatedText
   }
}