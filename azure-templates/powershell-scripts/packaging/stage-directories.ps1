param(
    [string]$controlFileName
)
If ("$(Get-Variable env:CD_PACKAGEBUILT_$env:CD_LABVIEW_VERSION -ValueOnly -ErrorAction SilentlyContinue)" -ne "True")
{
  # LabVIEW Version needs to be overwritten in packaging since it is not specified during pre-build
  Write-Output "Starting package for $env:CD_LABVIEW_VERSION..."

  Write-Output "setting up nipkg directory..."
  If (Test-Path "$env:CD_NIPKG_PATH")
  {
    Remove-Item -Path "$env:CD_NIPKG_PATH" -Recurse -Force
  }
  New-Item -Path "$env:CD_NIPKG_PATH" -ItemType "Directory"
  New-Item -Path "$env:CD_NIPKG_PATH" -Name "control" -ItemType "Directory"
  New-Item -Path "$env:CD_NIPKG_PATH" -Name "data" -ItemType "Directory"
  New-Item -Path "$env:CD_NIPKG_PATH" -Name "debian-binary" -ItemType "File"
  Set-Content "$env:CD_NIPKG_PATH\debian-binary" "2.0\n"
  Copy-Item `
    -Path "$env:CD_REPOSITORY\$controlFileName" `
    -Destination "$env:CD_NIPKG_PATH\control\control"

  Write-Output "updating nipkg control version parameters..."
  $contents = (Get-Content -Path "$env:CD_NIPKG_PATH\control\control") `
    -replace "{veristand_version}", "$env:CD_LABVIEW_VERSION" `
    -replace "{labview_version}", "$env:CD_LABVIEW_VERSION" `
    -replace "{nipkg_version}", "$env:CD_RELEASE_VERSION.$env:CD_BUILDCOUNTER" `
    -replace "{display_version}", "$env:CD_RELEASE_VERSION" `
    -replace "{quarterly_display_version}", "$env:CD_RELEASE_QUARTERLY" `
    -replace "{labview_short_version}", "$env:CD_LABVIEW_SHORTVERSION" `
    -replace "{pkg_x86_bitness_suffix}", "$env:CD_NIPKG_X86SUFFIX"
  Write-Output $contents
  Set-Content -Value $contents -Path "$env:CD_NIPKG_PATH\control\control"
  Write-Host "##vso[task.setvariable variable=CD.InstallerPath]$env:CD_ARCHIVEPATH\$env:BUILD_BUILDNUMBER\$env:CD_LABVIEW_VERSION\installer"
}
Else
{
  Write-Output "This package was already built, so this step can be skipped."
}