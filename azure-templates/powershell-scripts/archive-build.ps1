$bitnessSpecificPath = "$env:CD_ARCHIVEPATH\$env:BUILD_BUILDNUMBER\$env:CD_LABVIEW_VERSION\$env:CD_ARCHITECTURE"
If (-not(Test-Path "$bitnessSpecificPath"))
{
  New-Item -Path "$bitnessSpecificPath" -ItemType 'Directory'
}
Copy-Item -Path "$env:CD_BUILDOUTPUTPATH\*" -Destination "$bitnessSpecificPath\" -Recurse