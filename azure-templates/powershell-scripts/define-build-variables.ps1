param(
    [string]$lvVersion,
    [string]$lvBitness,
     [string]$outputDir,
    [string]$releaseVersion,
    [string]$buildTools
)

# Set file paths for important directories
Write-Output "Setting $buildTools as the build tools repo..."
Write-Host "##vso[task.setvariable variable=buildTools]$buildTools"
Write-Output "Using `"$PWD`" as the workspace directory..."
Write-Host "##vso[task.setvariable variable=workspaceDirectory]$PWD"
Write-Output "Using `"$outputDir`" as the build output directory..."
Write-Host "##vso[task.setvariable variable=buildOutputPath]$env:CD_REPONAME\$outputDir"
Write-Host "##vso[task.setvariable variable=nipkgPath]$env:CD_REPONAME\nipkg"

# Set release information
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
If (-not($derivedQuarterlyReleaseVersion -match "20.*Q.*"))
{
    Write-Error "Release version is not valid.  Should include follow AA.B.C where AA=year, B=quarter(0,3,5,8), and C=patches."
}
Write-Output "Configuring release version to $releaseVersion and quarterlyReleaseVersion to $derivedQuarterlyReleaseVersion..."
Write-Host "##vso[task.setvariable variable=releaseVersion]$releaseVersion"
Write-Host "##vso[task.setvariable variable=quarterlyReleaseVersion]$derivedQuarterlyReleaseVersion"

# Set LabVIEW version information
Write-Host "##vso[task.setvariable variable=lvVersion]$lvVersion"
# When adding a new version of LabVIEW as an option in custom device pipelines, a 
# new If statement is needed below with relevant variables
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

# Set LabVIEW Bitness information
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

Write-Host "##vso[task.setvariable variable=lvCLICall]`
    LabVIEWCLI `
        -PortNumber 3363 `
        -LabVIEWPath `"$lvPath\LabVIEW.exe`" `
        -AdditionalOperationDirectory `"%cd%\$buildTools\lv\operations`" "