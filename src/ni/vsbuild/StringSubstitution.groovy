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

      // Potentially run multiple times to allow nested replacement strings,
      // e.g. "{ni-veristand-{veristand_version}} (>=labview_short_version.0.0)"
      // or "{AUTOVERSION_ni-labview-{labview_version}-x86}"
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
            "ni-veristand-2017": "89bad39c-da8d-4c78-ba1a-f5de0dafdc00",
            "ni-veristand-2018": "d8f1d4e0-3466-42b0-92da-bdad96c42600",
            // We cannot simply map to the upgrade code defined by LabVIEW 2017, because
            // LabVIEW 2017 SP1 defines a different one. Instead we need to OR them, so that the
            // presence of either is sufficient. This makes listing the dependency in a control file
            // slightly awkward since you won't list a version; we prefix the variable with AUTOVERSION_
            // to hopefully make this slightly clearer.
            "AUTOVERSION_ni-labview-2017-x86": "aa4b25c0-a1f6-472e-8f76-32e406b7e44d (>=17.0.0) | 7a80ea72-c69d-4740-afbf-326544b9462c (>=17.1.0)",
      ]

      // Update the upper bound with each additional year release that uses the same package name format
      // Once we no longer need the legacy codes (i.e. the minimum supported VeriStand version is 2019) we
      // can deprecate this function, and just use the package names and versions directly.
      for (int year = 19; year <= 20; year++)
      {
         substitutionStrings.put(
               "ni-veristand-20${year}".toString(),
               "ni-veristand-20${year}".toString(),
         )
      }
      for (int year = 18; year <= 20; year++)
      {
         substitutionStrings.put(
               "AUTOVERSION_ni-labview-20${year}-x86".toString(),
               "ni-labview-20${year}-x86 (>=${year}.0.0)".toString(),
         )
      }

      return substitutionStrings
   }
}
