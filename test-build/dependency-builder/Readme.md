DependencyBuilder.lvproj is what was used to manually generate the DependencyLibrary.lvlibp files that were manually copied to

\\nirvana\Measurements\VeristandAddons\dependency-test-1\*
and 
\\nirvana\Measurements\VeristandAddons\dependency-test-2\*

These dependencies are used in the test-build that is run when changes are committed to niveristand-custom-device-build-tools to test codepaths that search for dependencies.

To build the dependencies, you can use dependency-builder.bat on a system with LabVIEW 2023, LabVIEW 2024, LabVIEW 2025 and LabVIEW2026.  It will generate exports in dependency-test that can be copied into nirvana as desired.