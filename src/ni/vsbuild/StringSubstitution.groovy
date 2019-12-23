package ni.vsbuild

class StringSubstitution implements Serializable {

   public static String replaceStrings(text, lvVersion, additionalReplacements = [:]) {
      def replacements = [
            "labview_version": lvVersion,
            "veristand_version": lvVersion,
            "labview_short_version": lvVersion.substring(lvVersion.length() - 2),
      ]
      replacements << getLegacySubstitutionStrings()
      replacements << additionalReplacements

      // Potentially run multiple times to allow nested replacement strings, e.g. "{ni-veristand-{veristand_version}}"
      def updatedText = text
      def modified = true
      while (modified)
      {
         def initialText = updatedText
         replacements.each { expression, value ->
            updatedText = updatedText.replaceAll("\\{$expression\\}", value)
         }
         modified = initialText != updatedText
      }

      return updatedText
   }

   private static Map<String, String> getLegacySubstitutionStrings()
   {
      def substitutionStrings = [
            "ni-veristand-2016": "b8da4f0a-93cc-45f2-8fe8-5cf878017dc1",
            "ni-veristand-2017": "89bad39c-da8d-4c78-ba1a-f5de0dafdc00",
            "ni-veristand-2018": "d8f1d4e0-3466-42b0-92da-bdad96c42600",
            "ni-labview-2016-x86": "7e1db25d-3f73-4659-a889-2b88330ebec6",
            "ni-labview-2017-x86": "b0f7b394-1d4e-4350-9852-b5bdb2d64cb2",
      ]

      // Update the upper bound with each additional year release that uses the same package name format
      // Once we no longer need the legacy codes (i.e. the minimum supported VeriStand version is 2019) we
      // can deprecate or remove this function, and just use the package names directly.
      for (int year = 2019; year <= 2020; year++)
      {
         def veristandString = "ni-veristand-${year}".toString()
         substitutionStrings.put(veristandString, veristandString)
      }
      for (int year = 2018; year <= 2020; year++)
      {
         def labviewString = "ni-labview-${year}-x86".toString()
         substitutionStrings.put(labviewString, labviewString)
      }

      return substitutionStrings
   }
}
