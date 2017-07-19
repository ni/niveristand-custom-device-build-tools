# commonbuild
Contains a pipeline and common scripts used during the pipeline in order to build NI VeriStand Custom Devices.

## Usage
The main entry point is buildPipeline.groovy. This script assumes that it will be invoked from another script after that script has defined the necessary parameters. Typically, this will be from a Jenkinsfile located in the repo to be built.

These parameters are:
  - nodeLabel: Any required label for a node to be able to build a particular repo
  - lvVersions: A list of LabVIEW/VeriStand versions that the pipeline should build against
  - sourceVersion: The LabVIEW version the source for the add-on is saved in

This script further assumes that Jenkins is configured (via the Pipeline Shared Libraries plugin) to implicitly include https://github.com/buckd/commonbuild.

## Requirements
This script has been designed such that the calling component must define the behavior of different build stages. This allows use of the common pipeline, but enables arbitrary complexity of a build for a given add-on. The requirements for a repo to use the buildPipeline script are:
  - The repo **must** have a file located at *vars/buildSteps.groovy*
  - buildSteps.groovy **must** define the following constants/functions
     - BUILT_DIR: The directory where the the LabVIEW build spec places the output
     - ARCHIVE_DIR: The directory where the output should be archived
     - syncDependencies(): A function responsible for syncing/cloning remote repositories to the build machine
     - setupLv(lvVersion): A function responsible for copying any dependencies or setting any environment variables required to build or test for the given version of LabVIEW
     - prepareSource(lvVersion): A function responsible for configuring any settings required for correctly loading the LV source code in the specified lvVersion
     - build(lvVersion): A function responsible for sequencing the steps to build the LV source to the BUILT_DIR
     - codegen(lvVersion): A function responsible for generating any code required to run the build
