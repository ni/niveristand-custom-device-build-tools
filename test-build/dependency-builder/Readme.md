DependencyBuilder.lvproj is what was used to manually generate the DependencyLibrary.lvlibp files that were manually copied to

\\nirvana\Measurements\VeristandAddons\dependency-test-1\*
and 
\\nirvana\Measurements\VeristandAddons\dependency-test-2\*

These dependencies are used in the test-build that is run when changes are committed to niveristand-custom-device-build-tools to test codepaths that search for dependencies.

To build the dependencies, you can use dependency-builder.bat on a system with LabVIEW 2020, LabVIEW 2021, and LabVIEW 2023.  It will generate exports in dependency-test that can be copied into nirvana as desired.