def call(lvPath){
  echo "Copying configuration file to $lvPath"
  bat "copy /Y \"$WORKSPACE\\commonbuild\\config\\LabVIEW.ini\" \"$lvPath\\LabVIEW.ini\""
}
