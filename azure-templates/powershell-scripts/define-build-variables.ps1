param(
    [string]$lvVersion,
    [string]$lvBitness,
    [string]$outputDir,
    [string]$releaseVersion,
    [string]$buildTools
)

# Set file paths for important directories
Write-Output "Setting $buildTools as the build tools repo..."
Write-Host "##vso[task.setvariable variable=CD.BuildTools]$buildTools"
Write-Output "Using `"$PWD`" as the workspace directory..."
Write-Host "##vso[task.setvariable variable=CD.Workspace]$PWD"
Write-Output "Using `"$outputDir`" as the build output directory..."
Write-Host "##vso[task.setvariable variable=CD.BuildOutputPath]$env:CD_REPOSITORY\$outputDir"
Write-Host "##vso[task.setvariable variable=CD.Nipkg.Path]$env:CD_REPOSITORY\nipkg"

# Set release information
Write-Output "Determining quarterly release version..."
$releaseData = "$releaseVersion" -Split "\."
$majorVersion = $releaseData[0]
If ($releaseData[1] -eq "0")
{
    $derivedQuarterlyReleaseVersion = "20$($releaseData[0]) Q1"
}
If ($releaseData[1] -eq "3")
{
    $derivedQuarterlyReleaseVersion = "20$($releaseData[0]) Q2"
}
If ($releaseData[1] -eq "5")
{
    $derivedQuarterlyReleaseVersion = "20$($releaseData[0]) Q3"
}
If ($releaseData[1] -eq "8")
{
    $derivedQuarterlyReleaseVersion = "20$($releaseData[0]) Q4"
}
If ($releaseData[2] -ne "0")
{
    $derivedQuarterlyReleaseVersion = "$($derivedQuarterlyReleaseVersion) Patch $($releaseData[2])"
}
If (-not($derivedQuarterlyReleaseVersion -match "20.*Q.*"))
{
    Write-Error "Release version is not valid.  Should include follow AA.B.C where AA=year, B=quarter(0,3,5,8), and C=patches."
}
Write-Output "Configuring release version to `"$releaseVersion`" and quarterly release version to `"$derivedQuarterlyReleaseVersion`"..."
Write-Host "##vso[task.setvariable variable=CD.Release.Version]$releaseVersion"
Write-Host "##vso[task.setvariable variable=CD.Release.Quarterly]$derivedQuarterlyReleaseVersion"
Write-Host "##vso[task.setvariable variable=CD.Release.MajorVersion]$majorVersion"

# Set LabVIEW version information
Write-Host "##vso[task.setvariable variable=CD.LabVIEW.Version]$lvVersion"
Write-Host "##vso[task.setvariable variable=lvVersion]$lvVersion" # Keep legacy version for variables used in packaging
# When adding a new version of LabVIEW as an option in custom device pipelines, 
# a new If statement is needed below with relevant variables
If ("$lvVersion" -eq "2020")
{
    Write-Output "Setting variables for LabVIEW 2020..."
    Write-Host "##vso[task.setvariable variable=CD.LabVIEW.Config]8.0.0.0"
    Write-Host "##vso[task.setvariable variable=CD.LabVIEW.ShortVersion]20"
    Write-Host "##vso[task.setvariable variable=CD.LabVIEW.SupportPackageSuffix]labview-2020-support{pkg_x86_bitness_suffix}"
}
Elseif ("$lvVersion" -eq "2021")
{
    Write-Output "Setting variables for LabVIEW 2021..."
    Write-Host "##vso[task.setvariable variable=CD.LabVIEW.Config]9.0.0.0"
    Write-Host "##vso[task.setvariable variable=CD.LabVIEW.ShortVersion]21"
    Write-Host "##vso[task.setvariable variable=CD.LabVIEW.SupportPackageSuffix]labview-2021-support{pkg_x86_bitness_suffix}"
}
Elseif ("$lvVersion" -eq "2023")
{
    Write-Output "Setting variables for LabVIEW 2023..."
    Write-Host "##vso[task.setvariable variable=CD.LabVIEW.Config]10.0.0.0"
    Write-Host "##vso[task.setvariable variable=CD.LabVIEW.ShortVersion]23"
    Write-Host "##vso[task.setvariable variable=CD.LabVIEW.SupportPackageSuffix]labview-support"
}
Elseif ("$lvVersion" -eq "2024")
{
    Write-Output "Setting variables for LabVIEW 2024..."
    Write-Host "##vso[task.setvariable variable=CD.LabVIEW.Config]11.0.0.0"
    Write-Host "##vso[task.setvariable variable=CD.LabVIEW.ShortVersion]24"
    Write-Host "##vso[task.setvariable variable=CD.LabVIEW.SupportPackageSuffix]labview-support"
}
Elseif ("$lvVersion" -eq "2025")
{
    Write-Output "Setting variables for LabVIEW 2025..."
    Write-Host "##vso[task.setvariable variable=CD.LabVIEW.Config]12.0.0.0"
    Write-Host "##vso[task.setvariable variable=CD.LabVIEW.ShortVersion]25"
    Write-Host "##vso[task.setvariable variable=CD.LabVIEW.SupportPackageSuffix]labview-support"
}
Else
{
    Write-Error "Invalid LabVIEW version defined in pipeline.  Use either 2020, 2021, 2023, or 2024."
}

# Set LabVIEW Bitness information
If ("$lvBitness" -eq "32bit")
{
    Write-Output "Setting variables for 32-bit..."
    $lvPath = "C:\Program Files (x86)\National Instruments\LabVIEW $lvVersion\LabVIEW.exe"
    Write-Host "##vso[task.setvariable variable=CD.Architecture]x86"
    Write-Host "##vso[task.setvariable variable=CD.Nipkg.X86Suffix]-x86"
    Write-Host "##vso[task.setvariable variable=CD.Nipkg.X64Suffix]"
    Write-Host "##vso[task.setvariable variable=nipkgx64suffix]" # Keep legacy version for variables used in packaging
}
Elseif ("$lvBitness" -eq '64bit')
{
    Write-Output "Setting variables for 64-bit..."
    $lvPath = "C:\Program Files\National Instruments\LabVIEW $lvVersion\LabVIEW.exe"
    Write-Host "##vso[task.setvariable variable=CD.Architecture]x64"
    Write-Host "##vso[task.setvariable variable=CD.Nipkg.X86Suffix]"
    Write-Host "##vso[task.setvariable variable=CD.Nipkg.X64Suffix]64"
    Write-Host "##vso[task.setvariable variable=nipkgx64suffix]64" # Keep legacy version for variables used in packaging
}
Else
{
    Write-Error "Invalid Bitness defined in pipeline.  Use either 32bit or 64bit."
}

$operations = "%cd%\$buildTools\lv\operations"
Write-Host "##vso[task.setvariable variable=CD.LabVIEWCLI]LabVIEWCLI -PortNumber 3363 -LabVIEWPath `"$lvPath`" -AdditionalOperationDirectory `"$operations`" "
