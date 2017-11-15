def call(String message) {
   currentBuild.result = "FAILURE"
   error "Build failed: $message"
}
