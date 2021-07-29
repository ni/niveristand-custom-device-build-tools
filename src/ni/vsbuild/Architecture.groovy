package ni.vsbuild

enum Architecture {
   x86('PROGRAMFILES(x86)'),
   x64('PROGRAMFILES')

   private final String programFilesVar

   Architecture(String value) {
      this.programFilesVar = value
   }

   public String getProgramFilesVar() {
      return this.programFilesVar
   }
}
