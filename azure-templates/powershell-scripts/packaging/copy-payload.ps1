param(
    [string]$installDir,
    [string]$payloadDir
)
If ("$(Get-Variable "env:CD_PACKAGEBUILT_$env:CD_LABVIEW_VERSION" -ValueOnly -ErrorAction SilentlyContinue)" -ne "True")
{
  If (-not(Test-Path "nipkg\data\$payloadDir"))
  {
    New-Item -Path "nipkg\data\$payloadDir" -ItemType "Directory"
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
      Copy-Item `
      -Path "$bitnessSpecificPath\*" `
      -Destination "nipkg\data\$installDir" `
      -Recurse
    }
  }
}
Else
{
  Write-Output "This package was already built, so this step can be skipped."
}