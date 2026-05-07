# Sign files before archiving
$niSignToolPath = 'C:\NISignTool\nisigntool.exe'
if (-not (Test-Path "$niSignToolPath")) {
  Write-Error "nisigntool not found at: $niSignToolPath"
  exit 1
}

$signableExtensions = @('.lvlibp', '.exe', '.dll')
$filesToSign = Get-ChildItem -Path "$env:CD_BUILDOUTPUTPATH" -Recurse -File | Where-Object { $_.Extension -in $signableExtensions }

foreach ($file in $filesToSign) {
  Write-Host "Signing file: $($file.FullName)"
  & "$niSignToolPath" "$($file.FullName)"
  if ($LASTEXITCODE -ne 0) {
    Write-Error "Failed to sign file: $($file.FullName)"
    exit 1
  }
  else {
    Write-Host "Successfully signed file: $($file.FullName)"
  }
}

$bitnessSpecificPath = "$env:CD_ARCHIVEPATH\$env:BUILD_BUILDNUMBER\$env:CD_LABVIEW_VERSION\$env:CD_ARCHITECTURE"
If (-not(Test-Path "$bitnessSpecificPath"))
{
  New-Item -Path "$bitnessSpecificPath" -ItemType 'Directory'
}
Copy-Item -Path "$env:CD_BUILDOUTPUTPATH\*" -Destination "$bitnessSpecificPath\" -Recurse
