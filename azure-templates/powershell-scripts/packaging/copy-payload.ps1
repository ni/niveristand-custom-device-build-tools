param(
    [string]$installDir,
    [string]$payloadDir
)

If ("$env:CD_PACKAGE_FINISHED" -match "$env:CD_LABVIEW_VERSION")
{
  Write-Output "Package already built for this version of LabVIEW... skipping this step."
  Write-Host "##vso[task.setvariable variable=CD.SkipPacking]True"
}
Else
{
  If (-not(Test-Path "$env:CD_NIPKG_PATH\data\$installDir"))
  {
    Write-Output "install location does not exist in nipkg/data directory... Adding location."
    New-Item -Path "$env:CD_NIPKG_PATH\data\$installDir" -ItemType "Directory"
  }

  $bitnessSpecificPaths = @(
    "$env:CD_ARCHIVEPATH\$env:BUILD_BUILDNUMBER\$env:CD_LABVIEW_VERSION\x86\$payloadDir",
    "$env:CD_ARCHIVEPATH\$env:BUILD_BUILDNUMBER\$env:CD_LABVIEW_VERSION\x64\$payloadDir"
  )
  # some of the payloads may be in x86, and some in x64, so copy from both
  ForEach ($bitnessSpecificPath in $bitnessSpecificPaths)
  {
    If (Test-Path "$bitnessSpecificPath")
    {
      Write-Output "copying payload from `"$bitnessSpecificPath`" into nipkg/data install directory..."
      Copy-Item `
      -Path "$bitnessSpecificPath\*" `
      -Destination "$env:CD_NIPKG_PATH\data\$installDir" `
      -Recurse
    }
    Else
    {
      Write-Output "no payload at `"$bitnessSpecificPath`" so skipping that location."
    }
  }
  Write-Host "##vso[task.setvariable variable=CD.SkipPacking]False"
}