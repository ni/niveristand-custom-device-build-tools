If (-not(Test-Path $env:CD_INSTALLERPATH))
{
  New-Item -Path $env:CD_INSTALLERPATH -ItemType "Directory"
}
Copy-Item -Path 'nipkg\*' -Destination $env:CD_INSTALLERPATH -Include *.nipkg -Recurse