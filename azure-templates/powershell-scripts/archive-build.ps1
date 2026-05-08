# Sign files before archiving
$niSignToolPath = 'C:\NISignTool\nisigntool.exe'
if (-not (Test-Path "$niSignToolPath")) {
  Write-Error "nisigntool not found at: $niSignToolPath"
  exit 1
}

$signableExtensions = @('.lvlibp', '.exe', '.dll')
$filesToSign = Get-ChildItem -Path "$env:CD_BUILDOUTPUTPATH" -Recurse -File |
  Where-Object {
    $_.Extension -in $signableExtensions -and
    $_.FullName -notmatch '(?i)\\linux'
  }
$failedSignings = @()
foreach ($file in $filesToSign) {
  Write-Host "Attempting to sign file: $($file.FullName)"
  & "$niSignToolPath" "$($file.FullName)"
  if ($LASTEXITCODE -ne 0) {
    Write-Warning "Signing failed (continuing build): $($file.FullName)"
    $failedSignings += $file.FullName
    continue
  }
  Write-Host "Successfully signed file: $($file.FullName)"
}

if ($failedSignings.Count -gt 0) {
  Write-Warning "Some files could not be signed. This may be expected for unsupported binaries."
  $failedSignings | ForEach-Object {
    Write-Warning "  $_"
  }
}

$bitnessSpecificPath = "$env:CD_ARCHIVEPATH\$env:BUILD_BUILDNUMBER\$env:CD_LABVIEW_VERSION\$env:CD_ARCHITECTURE"
If (-not(Test-Path "$bitnessSpecificPath"))
{
  New-Item -Path "$bitnessSpecificPath" -ItemType 'Directory'
}
Copy-Item -Path "$env:CD_BUILDOUTPUTPATH\*" -Destination "$bitnessSpecificPath\" -Recurse
