def call() {
   try {
      bat "taskkill /IM labview.exe /F"
   } catch (ignored) {
   }
}
