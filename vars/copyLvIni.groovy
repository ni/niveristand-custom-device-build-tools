def call(lvPath){
   echo "Copying configuration file to $lvPath"
   bat "copy /Y \"$WORKSPACE\\niveristand-custom-device-build-tools\\resources\\LabVIEW.ini\" \"$lvPath\\LabVIEW.ini\""
}
