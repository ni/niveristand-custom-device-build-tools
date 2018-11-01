def call(args, lvVersion){
   def versionPath = env."labviewPath_${lvVersion}"
   bat "LabVIEWCLI -LabVIEWPath \"${versionPath}\" -AdditionalOperationDirectory \"$WORKSPACE\\niveristand-custom-device-build-tools\\lv\\operations\" $args"
   lvCloseLabVIEW()
}
