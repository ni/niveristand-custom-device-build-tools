If (-not(Test-Path "$env:CD_INSTALLERPATH"))
{
  Write-Output "Installer location does not exist... Adding location."
  New-Item -Path "$env:CD_INSTALLERPATH" -ItemType "Directory"
}
Write-Output "Copying from NIPKG to installer location..."
Copy-Item -Path "$env:CD_NIPKG_PATH\*" -Destination "$env:CD_INSTALLERPATH" -Include "*.nipkg" -Recurse
