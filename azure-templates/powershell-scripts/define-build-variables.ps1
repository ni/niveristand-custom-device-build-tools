Write-Output "Defining repository variables..."
Write-Host '##vso[task.setvariable variable=buildTools]niveristand-custom-device-build-tools'
Write-Host "##vso[task.setvariable variable=workspaceDirectory]$PWD"
If ('$(Build.Reason)' -eq 'PullRequest')
{
    Write-Output "Setting variables for Pull Requests..."
    $sourceBranch = "$(System.PullRequest.SourceBranch)"
    Write-Output "Source branch $(System.PullRequest.SourceBranch)"
}
Else
{
    Write-Output "Setting variables for general builds..."
    $sourceBranch = "$(Build.SourceBranch)" -replace 'refs/heads/', ''
    Write-Output "Source branch $(Build.SourceBranch), removed refs/heads/"
}
If (Test-Path -Path "${{ parameters.archiveLocation }}\NI\export\$sourceBranch\norebuild")
{
    Write-Output "norebuild found in this source branch, so skipping this build..."
    Exit 1
}
Write-Output "Using $sourceBranch in archive path..."
Write-Host "##vso[task.setvariable variable=sourceBranch]$sourceBranch"
Write-Host "##vso[task.setvariable variable=archivePath]${{ parameters.archiveLocation }}\NI\export\$sourceBranch\"
$customDeviceRepoName = '$(Build.Repository.Name)' -replace '.+\/', ''
Write-Host "##vso[task.setvariable variable=customDeviceRepoName]$customDeviceRepoName"
Write-Host "##vso[task.setvariable variable=buildOutputPath]$customDeviceRepoName\${{ parameters.buildOutputLocation }}"
Write-Host "##vso[task.setvariable variable=nipkgPath]$customDeviceRepoName\nipkg"
`
Write-Output 'Determining quarterlyReleaseVersion...'
$releaseData = "${{ parameters.releaseVersion }}" -Split "\."
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
Write-Output "Configuring release version to ${{ parameters.releaseVersion }} and quarterlyReleaseVersion to $($derivedQuarterlyReleaseVersion)..."
Write-Host "##vso[task.setvariable variable=releaseVersion]${{ parameters.releaseVersion }}"
Write-Host "##vso[task.setvariable variable=quarterlyReleaseVersion]$derivedQuarterlyReleaseVersion"
Write-Host "##vso[task.setvariable variable=lvVersion]${{ parameters.lvVersionToBuild.version }}"
If ('${{ parameters.lvVersionToBuild.bitness }}' -eq '32bit')
{
    Write-Output "Setting variables for 32-bit..."
    Write-Host '##vso[task.setvariable variable=lvPath]C:\Program Files (x86)\National Instruments\LabVIEW ${{ parameters.lvVersionToBuild.version }}'
    Write-Host '##vso[task.setvariable variable=architecture]x86'
    Write-Host '##vso[task.setvariable variable=nipkgx86suffix]-x86'
    Write-Host '##vso[task.setvariable variable=nipkgx64suffix]'
}
Elseif ('${{ parameters.lvVersionToBuild.bitness }}' -eq '64bit')
{
    Write-Output "Setting variables for 64-bit..."
    Write-Host '##vso[task.setvariable variable=lvPath]C:\Program Files\National Instruments\LabVIEW ${{ parameters.lvVersionToBuild.version }}'
    Write-Host '##vso[task.setvariable variable=architecture]x64'
    Write-Host '##vso[task.setvariable variable=nipkgx86suffix]'
    Write-Host '##vso[task.setvariable variable=nipkgx64suffix]64'
}
Else
{
    Write-Error "Invalid Bitness defined in pipeline.  Use either 32bit or 64bit."
}
`
If ('${{ parameters.lvVersionToBuild.version }}' -eq '2020')
{
    Write-Output "Setting variables for LabVIEW 2020..."
    Write-Host '##vso[task.setvariable variable=lvConfigVersion]8.0.0.0'
    Write-Host '##vso[task.setvariable variable=shortLvVersion]20'
}
Elseif ('${{ parameters.lvVersionToBuild.version }}' -eq '2021')
{
    Write-Output "Setting variables for LabVIEW 2021..."
    Write-Host '##vso[task.setvariable variable=lvConfigVersion]9.0.0.0'
    Write-Host '##vso[task.setvariable variable=shortLvVersion]21'
}
Elseif ('${{ parameters.lvVersionToBuild.version }}' -eq '2023')
{
    Write-Output "Setting variables for LabVIEW 2023..."
    Write-Host '##vso[task.setvariable variable=lvConfigVersion]10.0.0.0'
    Write-Host '##vso[task.setvariable variable=shortLvVersion]23'
}
Else
{
    Write-Error "Invalid LabVIEW version defined in pipeline.  Use either 2020, 2021, or 2023."
}