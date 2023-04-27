param(
    [string]$installDir,
    [string]$payloadDir
)

If (-not(Test-Path "$env:CD_NIPKG_PATH\data\$payloadDir"))
{
  Write-Output "payload location does not exist in nipkg/data directory... Adding location."
  New-Item -Path "$env:CD_NIPKG_PATH\data\$payloadDir" -ItemType "Directory"
}

$bitnessSpecificPaths = @(
  "$env:CD_ARCHIVEPATH\$env:BUILD_BUILDNUMBER\$env:CD_LABVIEW_VERSION\x86\$payloadDir",
  "$env:CD_ARCHIVEPATH\$env:BUILD_BUILDNUMBER\$env:CD_LABVIEW_VERSION\x64\$payloadDir"
)
# some of the payloads may be in x86, and some in x64, so copy from both
ForEach ($bitnessSpecificPath in $bitnessSpecificPaths)
{
  If (-not(Test-Path "$bitnessSpecificPath"))
  {
    Write-Output "copying payload from `"$bitnessSpecificPath`" into nipkg/data install directory..."
    Copy-Item `
    -Path "$bitnessSpecificPath\*" `
    -Destination "$env:CD_NIPKG_PATH\data\$installDir" `
    -Recurse
  }
  }
