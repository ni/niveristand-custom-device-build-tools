param(
  [string]$archiveDir
)

If (-not(Test-Path -Path "$archiveDir\NI\export\main"))
{
  Write-Output "`"main`" has not been built yet, skipping this step..."
}
Else
{
  Write-Output "Finding latest successful build of `"main`" for comparison..."
  $allBuildsOfMain = Get-ChildItem `
    -Path "$archiveDir\NI\export\main\*" `
    | Sort-Object {$_.Name} -Descending
  $mainPath = "Undefined"
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
  If ($mainPath -eq "Undefined")
  {
    Write-Output "No builds were found in main to compare, so skipping the rest of this step..."
  }
  Else
  {
    Write-Output "Getting packages in `"$env:CD_ARCHIVEPATH\$env:BUILD_BUILDNUMBER`"..."
    $allPackagesThisBuild = Get-ChildItem -Recurse -Path "$env:CD_ARCHIVEPATH\$env:BUILD_BUILDNUMBER\*\installer\*.nipkg"
    Write-Output "Total packages in this build: $($allPackagesThisBuild.Count)"

    Write-Output "Getting packages in `"$mainPath`"..."
    $allPackagesMain = Get-ChildItem -Recurse -Path "$mainPath\*\installer\*.nipkg"
    Write-Output "Total packages in main: $($allPackagesMain.Count)"

    ForEach ($package in $allPackagesThisBuild)
    {
      $matchingPackage = $allPackagesMain | Where-Object {$_.Name -match "$($package.Name.Split("_")[0])_"}
      Write-Output "Unpacking $($package.FullName) and comparing to $($matchingPackage.FullName) from `"main`"..."

      $validateDirectory = "$PWD\Validate"
      $validateOutputs = "$validateDirectory\Outputs"
      New-Item -Path "$validateOutputs" -ItemType "Directory" | Out-Null
      tar -xf "$package" -C "$validateDirectory"
      tar -xf "$validateDirectory\data.tar.gz" -C "$validateOutputs"
      $filesInValidatePackage = Get-ChildItem -Recurse -Path "$validateOutputs" -File
      If ($filesInValidatePackage)
      {
        $validateFileCount = $filesInValidatePackage.Count
        $validateFileSize = ($filesInValidatePackage | Measure-Object -Property "Length" -Sum).Sum  
      }
      Else 
      {
        $validateFileCount = $validateFileSize = 0
      }

      $compareDirectory = "$PWD\Compare"
      $compareOutputs = "$compareDirectory\Outputs"
      New-Item -Path "$compareOutputs" -ItemType "Directory" | Out-Null
      tar -xf "$matchingPackage" -C "$compareDirectory"
      tar -xf "$compareDirectory\data.tar.gz" -C "$compareOutputs"
      $filesInComparePackage = Get-ChildItem -Recurse -Path "$compareOutputs" -File
      If ($filesInComparePackage)
      {
        $compareFileCount = $filesInComparePackage.Count
        $compareFileSize = ($filesInComparePackage | Measure-Object -Property "Length" -Sum).Sum  
      }
      Else 
      {
        $compareFileCount = $compareFileSize = 0
      }

      Write-Output "             ________________________________"
      Write-Output "             | FILE COUNT | FILE SIZE (SUM) |"
      Write-Output "        main | $($compareFileCount.ToString().PadLeft(10, " ")) | $($compareFileSize.ToString().PadLeft(15, " ")) |"
      Write-Output "  this build | $($validateFileCount.ToString().PadLeft(10, " ")) | $($validateFileSize.ToString().PadLeft(15, " ")) |"
      Write-Output "             ________________________________"
      
      Remove-Item -Recurse -Path "$validateDirectory"
      Remove-Item -Recurse -Path "$compareDirectory"
      If ($validateFileCount -eq 0)
      {
        Write-Output "this build's package file `"$($package.Name)`" has no files in it, so validation has failed.  Exiting..."
        Exit 1
      }
    }
  }
}