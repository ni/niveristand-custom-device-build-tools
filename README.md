# NI VeriStand Custom Device Build Tools
The **niveristand-custom-device-build-tools** repository provides a common set of tools to automate building NI VeriStand custom devices using [Azure Pipelines](https://azure.microsoft.com/en-us/products/devops/pipelines). The intended audience includes custom device developers and integrators.

## Jenkins Branch Archive
The main branch of this repository has been updated to support Azure Pipelines. The [jenkins branch](https://github.com/ni/niveristand-custom-device-build-tools/tree/jenkins) contains the build pipeline steps for Jenkins automation, and will no longer be actively updated for new versions of VeriStand.

## Usage
The azure-pipelines.yaml file contains the configuration for builds using the stages defined in this repo. See the [documentation](https://github.com/ni/niveristand-custom-device-build-tools/blob/main/add-docs/docs/Azure%20Pipeline%20YAML.md) for supported properties.

## LabVIEW Version
The LabVIEW source for this repository is saved for LabVIEW 2020, but is forward compatible to newer versions.

## Dependencies
The following top-level dependencies are required on the build machine to use the repository:

- [LabVIEW Professional Development System](http:/ni.com/labview)
- [LabVIEW Command Line Interface](http://www.ni.com/en-us/support/downloads/software-products/download.ni-labview-command-line-interface.html)
- [Python](https://www.python.org/downloads/) (Version 3.7 or later)

## Git History & Rebasing Policy
Branch rebasing and other history modifications will be listed here, with several notable exceptions:
- Branches prefixed with `dev/` may be rebased, overwritten, or deleted at any time.
- Pull requests may be squashed on merge.

## License
The NI VeriStand Custom Device Build Tools are licensed under an MIT-style license (see LICENSE). Other incorporated projects may be licensed under different licenses. All licenses allow for non-commercial and commercial use.
