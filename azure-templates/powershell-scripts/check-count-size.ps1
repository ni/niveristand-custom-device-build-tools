param(
  [string]$archiveDir
)
Write-Output "Finding latest successful build of `"main`" for comparison..."
$allBuildsOfMain = Get-ChildItem `
  -Path "$archiveDir\NI\export\main\*" `
  | Sort-Object {$_.Name} -Descending
ForEach ($build in $allBuildsOfMain)
{
  Write-Output "Checking $build..."
  If (Test-Path -Path "$build")
  {
    Write-Output "Checking for .finished file..."
    If (Test-Path -Path "$build\.finished")
    {
      Write-Output "latest successful build: $build"
      $mainPath = $build
      Break
    }
    Else
    {
      Write-Output ".finished file not found at $build, checking next build..."
    }
  }
}
Write-Output "Getting packages in `"$env:CD_ARCHIVEPATH\$env:BUILD_BUILDNUMBER`"..."
$allPackagesThisBuild = Get-ChildItem -Recurse -Path "$env:CD_ARCHIVEPATH\$env:BUILD_BUILDNUMBER\*\installer\*.nipkg"
Write-Output "Total packages in this build: $($allPackagesThisBuild.Count)"

Write-Output "Getting packages in `"$mainPath`"..."
$allPackagesMain = Get-ChildItem -Recurse -Path "$mainPath\*\installer\*.nipkg"
Write-Output "Total packages in main: $($allPackagesMain.Count)"

ForEach ($package in $allPackagesThisBuild)
{
  $matchingPackage = $allPackagesMain | Where-Object {$_.Name -match ($package.Name.Split("_")[0])}
  $index = [array]::IndexOf($allPackagesThisBuild, $package)
  Write-Output "Unpacking $($package.FullName) and comparing to $($matchingPackage.FullName) from `"main`"..."

  $validateDirectory = "$PWD\Validate-$index"
  $validateOutputs = "$validateDirectory\Outputs"
  New-Item -Path "$validateOutputs" -ItemType "Directory" | Out-Null
  tar -xf "$package" -C "$validateDirectory"
  tar -xf "$validateDirectory\data.tar.gz" -C "$validateOutputs"
  $filesInValidatePackage = Get-ChildItem -Recurse -Path "$validateOutputs" -File
  $validateFileCount = $filesInValidatePackage.Count
  $validateFileSize = ($filesInValidatePackage | Measure-Object -Property "Length" -Sum).Sum

  $compareDirectory = "$PWD\Compare-$index"
  $compareOutputs = "$compareDirectory\Outputs"
  New-Item -Path "$compareOutputs" -ItemType "Directory" | Out-Null
  tar -xf "$matchingPackage" -C "$compareDirectory"
  tar -xf "$compareDirectory\data.tar.gz" -C "$compareOutputs"
  $filesInComparePackage = Get-ChildItem -Recurse -Path "$compareOutputs" -File
  $compareFileCount = $filesInComparePackage.Count
  $compareFileSize = ($filesInComparePackage | Measure-Object -Property "Length" -Sum).Sum

  If (-and($compareFileCount, $validateFileCount, $compareFileSize, $validateFileSize))
  {
    Write-Output "            ________________________________"
    Write-Output "            | FILE COUNT | FILE SIZE (SUM) |"
    Write-Output "  main      | $($compareFileCount.ToString().PadLeft(10, " ")) | $($compareFileSize.ToString().PadLeft(15, " ")) |"
    Write-Output "  thisBuild | $($validateFileCount.ToString().PadLeft(10, " ")) | $($validateFileSize.ToString().PadLeft(15, " ")) |"
    Write-Output "            ________________________________"
  }
}