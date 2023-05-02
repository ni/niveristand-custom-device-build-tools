param([string]$archiveDir)

$repoName = "$env:BUILD_REPOSITORY_NAME" -replace ".+\/", ""
Write-Output "Defining custom device repo name as $repoName..."
Write-Host "##vso[task.setvariable variable=CD.Repository]$repoName"

If ("$env:BUILD_REASON" -eq "PullRequest")
{
    Write-Output "Setting variables for Pull Requests..."
    $sourceBranch = "$env:SYSTEM_PULLREQUEST_SOURCEBRANCH"
    Write-Output "Source branch set to `"$sourceBranch`""
}
Else
{
    Write-Output "Setting variables for CI/Manual builds..."
    $sourceBranch = "$env:BUILD_SOURCEBRANCH" -replace "refs/heads/", ""
    Write-Output "Source branch set to `"$sourceBranch`", removed `"refs/heads/`""
}
Write-Host "##vso[task.setvariable variable=CD.SourceBranch]$sourceBranch"

If (Test-Path -Path "$archiveDir\NI\export\$sourceBranch\norebuild")
{
    Write-Output "norebuild found in `"$sourceBranch`", so skipping this build... Ending build now."
    Exit 1
}

$archiveFullPath = "$archiveDir\NI\export\$sourceBranch"
Write-Output "Using `"$archiveFullPath`" as archive path..."
Write-Host "##vso[task.setvariable variable=CD.ArchivePath]$archiveFullPath"