If ("$(Get-Variable "env:CD_PACKAGEBUILT_$env:CD_LABVIEW_VERSION" -ValueOnly -ErrorAction SilentlyContinue)" -ne "True")
{
  If (-not(Test-Path $env:CD_INSTALLERPATH))
  {
    New-Item -Path $env:CD_INSTALLERPATH -ItemType "Directory"
  }
  Copy-Item -Path 'nipkg\*' -Destination $env:CD_INSTALLERPATH -Include *.nipkg -Recurse
}
Else
{
  Write-Output "This package was already built, so this step can be skipped."
}