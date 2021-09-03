package ni.vsbuild

enum Architecture {
   x86('PROGRAMFILES(x86)'),
   x64('PROGRAMFILES')

   public final String programFilesVar

   Architecture(String value) {
      this.programFilesVar = value
   }

   @NonCPS
   static Architecture bitnessToArchitecture(int bitness) {
      return bitness == 32 ? Architecture.x86 : Architecture.x64
   }

   @NonCPS
   static int architectureToBitness(Architecture arch) {
      return arch == Architecture.x86 ? 32 : 64
   }
}
