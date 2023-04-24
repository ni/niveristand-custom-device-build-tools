param(
    [string]$source,
    [string]$file,
    [string]$destination,
    [string]$silence
)

If ("$env:CD_SKIPTHISSTEP" -eq "True")
{
    Write-Output "dependency copying was skipped because a matching exclusion was found for this build step."
}
Else
{
  Write-Output "Configuring target directory environment..."
  If ("$env:CD_BUILDTARGET" -eq "My Computer")
  {
    $file = $file -replace '\$target', "Windows"
    Write-Output "Setting dependency environment `$target to `"Windows`" - new dependency path is $file..."
  }
  Elseif ("$env:CD_BUILDTARGET" -eq "Linux x64")
  {
    $file = $file -replace '\$target', "Linux_x64"
    Write-Output "Setting dependency environment `$target to `"Linux_x64`" - new dependency path is $file..."
  }
  
  $branchName = "main"
  If ("$env:CD_SOURCEBRANCH" -ne "$branchName")
  {
    Write-Output "Checking for builds of `"$env:CD_SOURCEBRANCH`" branch..."
    If (Test-Path -Path "$source\NI\export\$env:CD_SOURCEBRANCH\*\$env:CD_LABVIEW_VERSION\$env:CD_ARCHITECTURE")
    {
      Write-Output "branch was found, using $env:CD_SOURCEBRANCH instead of $branchName..."
      $branchName = "$env:CD_SOURCEBRANCH"
    }
    Else
    {
      Write-Output "branch not found, using a build of `"$branchName`"..."
    }
  }
  Else
  {
    Write-Output "Using `"$branchName`" branch..."
  }
  Write-Output "Finding latest successful build of dependency `"$file`"..."
  $allDependencyBuilds = Get-ChildItem `
    -Path "$source\NI\export\$branchName\*" `
    | Sort-Object {$_.Name} -Descending
  Foreach ($build in $allDependencyBuilds)
  {
    Write-Output "Checking $build..."
    If (Test-Path -Path "$build\$env:CD_LABVIEW_VERSION\$env:CD_ARCHITECTURE\")
    {
      Write-Output "Checking for .finished file..."
      If (Test-Path -Path "$build\.finished")
      {
        Write-Output "latest successful build: $build"
        $dependencyFilePath = "$build\$env:CD_LABVIEW_VERSION\$env:CD_ARCHITECTURE\$file"
        Break
      }
      Else
      {
        Write-Output ".finished file not found at $build"
      }
    }
  }

  Write-Output "Copying dependency $file..."
  If (-not(Test-Path -Path "$env:CD_WORKSPACE\$env:CD_REPOSITORY\$destination"))
  {
    New-Item `
      -Path "$env:CD_WORKSPACE\$env:CD_REPOSITORY\$destination" `
      -ItemType 'Directory'
  }
  If (Test-Path $dependencyFilePath)
  {
    Copy-Item `
      -Path $dependencyFilePath `
      -Destination "$env:CD_WORKSPACE\$env:CD_REPOSITORY\$destination" `
      -Recurse `
      -Force
  }
  Else
  {
    If ($silence -eq "true")
    {
      Write-Output "Dependency does not exist at $dependencyFilePath. Dependency error silenced, moving on..."
    }
    Else 
    {
      Write-Error "Dependency does not exist at $dependencyFilePath."
    }
  }
}