If ("$(Get-Variable "env:CD_PACKAGEBUILT_$env:CD_LABVIEW_VERSION" -ValueOnly -ErrorAction SilentlyContinue)" -ne "True")
{
  If ( ("$env:CD_SOURCEBRANCH" -match "release") -and (Test-Path $env:CD_INSTALLERPATH) )
  {
    Write-Output "Adding manifest file to release branch installer directory"
    $jsonFilePath = Join-Path $env:CD_INSTALLERPATH "manifest.json"
    $scmObject = [PSCustomObject]@{
      GIT_BRANCH = "$env:CD_SOURCEBRANCH"
      GIT_COMMIT = "$env:BUILD_SOURCEVERSION"
      GIT_URL = "$env:BUILD_REPOSITORY_URI.git"
    }
    $outputObject = [PSCustomObject]@{
      scm = $scmObject
    }
    $jsonString = $outputObject | ConvertTo-Json
    Set-Content -Path $jsonFilePath -Value $jsonString
  }
  Else
  {
    Write-Output "No release branch packages built; skipping manifest file"
  }

  If (-not("$env:CD_SOURCEBRANCH" -match "release"))
  {
    $cleanedSourceBranch = "$env:CD_SOURCEBRANCH" -replace "\/", "_"
    Write-Output "Not a release branch, so appending branch name $cleanedSourceBranch to nipkg files..."
    $packages = Get-ChildItem `
      -Path "$env:CD_INSTALLERPATH\*.nipkg"
    Foreach ($package in $packages)
    {
      If (-not("$packageFile" -match "$cleanedSourceBranch.nipkg"))
      {
        Rename-Item -Path "$packageFile" -NewName ("$packageFile" -replace ".nipkg", "_$cleanedSourceBranch.nipkg")
      }
    }
  }
  Write-Host "##vso[task.setvariable variable=CD.PackageBuilt.$env:CD_LABVIEW_VERSION]True"
}
Else
{
  Write-Output "This package was already built, so this step can be skipped."
}