Write-Output "All previous jobs complete.  Storing .finished file..."
New-Item -Path "$env:CD_ARCHIVEPATH\$env:BUILD_BUILDNUMBER" -Name ".finished" -ItemType "File"