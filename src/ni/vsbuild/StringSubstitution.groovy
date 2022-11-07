package ni.vsbuild

class StringSubstitution implements Serializable {

   public static String replaceStrings(text, lvVersion, additionalReplacements = [:]) {
      def year = lvVersion.lvRuntimeVersion
      def architecture = lvVersion.architecture
      def replacements = [
            "labview_version": year,
            "veristand_version": year,
            "labview_short_version": year.substring(year.length() - 2),
            "pkg_x86_bitness_suffix": architecture == Architecture.x86 ? "-x86" : "",
            "nipaths_64_bitness_suffix": architecture == Architecture.x64 ? "64" : "",
      ]

      switch (year) {
         case '2019':
            replacements.put("labview_support_package_suffix", "labview-2019-support{pkg_x86_bitness_suffix}");
            break;
         case '2020':
            replacements.put("labview_support_package_suffix", "labview-2020-support{pkg_x86_bitness_suffix}");
            break;
         case '2021':
            replacements.put("labview_support_package_suffix", "labview-2021-support{pkg_x86_bitness_suffix}");
            break;
         default:
            replacements.put("labview_support_package_suffix", "labview-support");
            break;
      }

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
}
