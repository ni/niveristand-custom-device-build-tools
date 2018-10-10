def call(args){
   bat "LabVIEWCLI -LabVIEWPath \"${env.labviewPath}\" -AdditionalOperationDirectory \"$WORKSPACE\\niveristand-custom-device-build-tools\\lv\\operations\" $args"
   lvCloseLabVIEW()
}
