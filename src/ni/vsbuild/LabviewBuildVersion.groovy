package ni.vsbuild

class LabviewBuildVersion implements Serializable {

   //private static Integer 32_BIT = 32
   //private static Integer 64_BIT = 64

   public static Integer DEFAULT_LABVIEW_BITNESS = 32

   public final String lvRuntimeVersion
   public final Architecture architecture

   LabviewBuildVersion(String lvRuntimeVersion, int bitness = 32) {
      this.lvRuntimeVersion = lvRuntimeVersion
      this.architecture = bitness == 32 ? Architecture.x86 : Architecture.x64
   }

   public String getLabviewPath() {
      def baseVersion = (lvRuntimeVersion =~ /^[0-9]+/).getAt(0)
      def progFilesDir = architecture.programFilesDir()
      return "$progFilesDir\\National Instruments\\LabVIEW ${baseVersion}\\LabVIEW.exe"
   }

   @NonCPS
   @Override
   public String toString() {
      def bitness = architecture == Architecture.x86 ? "(32-bit)" : "(64-bit)"
      return "$lvRuntimeVersion $bitness"
   }
}