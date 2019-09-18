package ni.vsbuild

class StringSubstitution implements Serializable {

   public static String replaceStrings(text, lvVersion, additionalReplacements = [:]) {
      def replacements = [
            "labview_version": lvVersion,
            "veristand_version": lvVersion,
            "labview_short_version": lvVersion[-2..-1],
      ]
      replacements << additionalReplacements

      def updatedText = text
      replacements.each { expression, value ->
         updatedText = updatedText.replaceAll("\\{$expression\\}", value)
      }

      return updatedText
   }
}