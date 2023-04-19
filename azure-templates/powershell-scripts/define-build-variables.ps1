param(
    [string]$lvVersion,
    [string]$lvBitness,
    [string]$archiveDir,
    [string]$outputDir,
    [string]$releaseVersion,
    [string]$buildTools
)

Write-Output "Defining repository variables for this job..."
Write-Host "##vso[task.setvariable variable=buildTools]$buildTools"
Write-Host "##vso[task.setvariable variable=workspaceDirectory]$PWD"
If ("$env:BUILD_REASON" -eq "PullRequest")
{
    Write-Output "Setting variables for Pull Requests..."
    $sourceBranch = "$env:SYSTEM_PULLREQUEST_SOURCEBRANCH"
    Write-Output "Source branch set to $sourceBranch"
}
Else
{
    Write-Output "Setting variables for CI/Manual builds..."
    $sourceBranch = "$env:BUILD_SOURCEBRANCH" -replace "refs/heads/", ""
    Write-Output "Source branch $sourceBranch, removed refs/heads/"
}
If (Test-Path -Path "$archiveDir\NI\export\$sourceBranch\norebuild")
{
    Write-Output "norebuild found in this source branch, so skipping this build... Ending build now..."
    Exit 1
}
Write-Output "Using $sourceBranch in archive path..."
Write-Host "##vso[task.setvariable variable=sourceBranch]$sourceBranch"
Write-Host "##vso[task.setvariable variable=archivePath]$archiveDir\NI\export\$sourceBranch\"
$customDeviceRepoName = "$env:BUILD_REPOSITORY_NAME" -replace ".+\/", ""
Write-Host "##vso[task.setvariable variable=customDeviceRepoName]$customDeviceRepoName"
Write-Host "##vso[task.setvariable variable=buildOutputPath]$customDeviceRepoName\$outputDir"
Write-Host "##vso[task.setvariable variable=nipkgPath]$customDeviceRepoName\nipkg"

Write-Output "Determining quarterlyReleaseVersion..."
$releaseData = "$releaseVersion" -Split "\."
If ($releaseData[1] -eq "0")
{
    $derivedQuarterlyReleaseVersion = "20$releaseData[0] Q1"
}
If ($releaseData[1] -eq "3")
{
$derivedQuarterlyReleaseVersion = "20$releaseData[0] Q2"
}
If ($releaseData[1] -eq "5")
{
    $derivedQuarterlyReleaseVersion = "20$releaseData[0] Q3"
}
If ($releaseData[1] -eq "8")
{
    $derivedQuarterlyReleaseVersion = "20$releaseData[0] Q4"
}
Write-Output "Configuring release version to $releaseVersion and quarterlyReleaseVersion to $derivedQuarterlyReleaseVersion..."
Write-Host "##vso[task.setvariable variable=releaseVersion]$parameters.releaseVersion"
Write-Host "##vso[task.setvariable variable=quarterlyReleaseVersion]$derivedQuarterlyReleaseVersion"
Write-Host "##vso[task.setvariable variable=lvVersion]$lvVersion"
If ("$lvBitness" -eq "32bit")
{
    Write-Output "Setting variables for 32-bit..."
    $lvPath = "C:\Program Files (x86)\National Instruments\LabVIEW $lvVersion"
    Write-Host "##vso[task.setvariable variable=architecture]x86"
    Write-Host "##vso[task.setvariable variable=nipkgx86suffix]-x86"
    Write-Host "##vso[task.setvariable variable=nipkgx64suffix]"
}
Elseif ("$lvBitness" -eq '64bit')
{
    Write-Output "Setting variables for 64-bit..."
    $lvPath = "C:\Program Files\National Instruments\LabVIEW $lvVersion"
    Write-Host "##vso[task.setvariable variable=architecture]x64"
    Write-Host "##vso[task.setvariable variable=nipkgx86suffix]"
    Write-Host "##vso[task.setvariable variable=nipkgx64suffix]64"
}
Else
{
    Write-Error "Invalid Bitness defined in pipeline.  Use either 32bit or 64bit."
}
Write-Host "##vso[task.setvariable variable=lvPath]$lvPath"

If ("$lvVersion" -eq "2020")
{
    Write-Output "Setting variables for LabVIEW 2020..."
    Write-Host "##vso[task.setvariable variable=lvConfigVersion]8.0.0.0"
    Write-Host "##vso[task.setvariable variable=shortLvVersion]20"
}
Elseif ("$lvVersion" -eq "2021")
{
    Write-Output "Setting variables for LabVIEW 2021..."
    Write-Host "##vso[task.setvariable variable=lvConfigVersion]9.0.0.0"
    Write-Host "##vso[task.setvariable variable=shortLvVersion]21"
}
Elseif ("$lvVersion" -eq "2023")
{
    Write-Output "Setting variables for LabVIEW 2023..."
    Write-Host "##vso[task.setvariable variable=lvConfigVersion]10.0.0.0"
    Write-Host "##vso[task.setvariable variable=shortLvVersion]23"
}
Else
{
    Write-Error "Invalid LabVIEW version defined in pipeline.  Use either 2020, 2021, or 2023."
}

Write-Host "##vso[task.setvariable variable=lvCLICall]LabVIEWCLI -PortNumber 3363 -LabVIEWPath `"$lvPath\LabVIEW.exe`" -AdditionalOperationDirectory `"%cd%\niveristand-custom-device-build-tools\lv\operations`" "