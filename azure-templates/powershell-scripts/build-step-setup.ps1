param(
    [string]$projectOverride = "No Overrides",
    [string]$projectPath,
    [string]$buildOperation,
    [string]$buildSpec,
    [string]$target
)

If ("$projectOverride" -eq "Absolute")
{
    Write-Host "##vso[task.setvariable variable=CD.AbsolutePath]True"
    $lvConfigFilePath = "$projectPath.config"
}
Else
{
    Write-Host "##vso[task.setvariable variable=CD.AbsolutePath]False"
    $lvConfigFilePath = "$env:CD_WORKSPACE\$env:CD_REPOSITORY\$projectPath.config"
}

If (-not(Test-Path -Path $lvConfigFilePath))
{
    Write-Output "adding .config file to project..."
    Copy-Item "$env:CD_BUILDTOOLS\resources\LabVIEW.exe.config" -Destination $lvConfigFilePath
    (Get-Content -Path $lvConfigFilePath) -replace "2016.0.0.0", "$env:CD_LABVIEW_CONFIG" | Set-Content -Path $lvConfigFilePath
}
`
If ("$buildOperation" -eq "ExecuteAllBuildSpecs")
{
    Write-Output "Configuring build operation for all targets and build specs..."
    Write-Host "##vso[task.setvariable variable=CD.TargetArg]"
    Write-Host "##vso[task.setvariable variable=CD.BuildSpecArg]"
}
Elseif ("$buildOperation" -eq "ExecuteBuildSpecAllTargets")
{
    Write-Output "Configuring build operation for all targets and single build spec..."
    Write-Host "##vso[task.setvariable variable=CD.TargetArg]"
    Write-Host "##vso[task.setvariable variable=CD.BuildSpecArg]-BuildSpecName `"$buildSpec`" "
}
Elseif ("$buildOperation" -eq "ExecuteBuildSpec")
{
    Write-Output "Configuring build operation for specific target and build spec..."
    Write-Host "##vso[task.setvariable variable=CD.TargetArg]-TargetName `"$target`" "
    Write-Host "##vso[task.setvariable variable=CD.BuildSpecArg]-BuildSpecName `"$buildSpec`" "
    Write-Host "##vso[task.setvariable variable=CD.BuildTarget]$target"
}
Else
{
    Write-Error "invalid buildStep.buildOperation provided.  Valid options are ExecuteBuildSpec, ExecuteBuildSpecAllTargets, and ExecuteAllBuildSpecs."
}
`
Write-Output "resetting build exclusion skip variable..."
Write-Host "##vso[task.setvariable variable=CD.SkipThisStep]False"
Write-Host "##vso[task.setvariable variable=CD.ProjectPath]$projectPath"
Write-Host "##vso[task.setvariable variable=CD.BuildOperation]$buildOperation"