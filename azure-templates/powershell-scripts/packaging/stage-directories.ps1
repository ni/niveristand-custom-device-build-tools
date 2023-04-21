param(
    [string]$controlFileName,
    [string]$lvVersion
)
# LabVIEW Version needs to be overwritten in packaging since it is not specified during pre-build
Write-Host "##vso[task.setvariable variable=CD.LabVIEW.Version]$lvVersion)"
Write-Output "Starting package for $env:CD_LABVIEW_VERSION..."

Write-Output "setting up nipkg directory..."
If (Test-Path "$(CD.Nipkg.Path)")
{
  Remove-Item -Path "$(CD.Nipkg.Path)" -Recurse -Force
}
New-Item -Path "$(CD.Nipkg.Path)" -ItemType "Directory"
New-Item -Path "$(CD.Nipkg.Path)" -Name "control" -ItemType "Directory"
New-Item -Path "$(CD.Nipkg.Path)" -Name "data" -ItemType "Directory"
New-Item -Path "$(CD.Nipkg.Path)" -Name "debian-binary" -ItemType "File"
Set-Content "$(CD.Nipkg.Path)\debian-binary" "2.0\n"
Copy-Item `
  -Path "$(CD.Repository)\${{ package.controlFileName }}" `
  -Destination "$(CD.Nipkg.Path)\control\control"

Write-Output "updating nipkg control version parameters..."
$contents = (Get-Content -Path "$(CD.Nipkg.Path)\control\control") `
  -replace "{veristand_version}", "$(CD.LabVIEW.Version)" `
  -replace "{labview_version}", "$(CD.LabVIEW.Version)" `
  -replace "{nipkg_version}", "$(CD.Release.Version).$env:CD_BUILDCOUNTER" `
  -replace "{display_version}", "$(CD.Release.Version)" `
  -replace "{quarterly_display_version}", "$(CD.Release.Quarterly)" `
  -replace "{labview_short_version}", "$(CD.LabVIEW.ShortVersion)" `
  -replace "{pkg_x86_bitness_suffix}", "$(CD.Nipkg.X86Suffix)"
Write-Output $contents
Set-Content -Value $contents -Path "$(CD.Nipkg.Path)\control\control"
Write-Host "##vso[task.setvariable variable=CD.InstallerPath]$env:CD_ARCHIVEPATH\$env:BUILD_BUILDNUMBER\$lvVersion\installer"