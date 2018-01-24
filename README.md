# Jenkins Pipeline Library for ni-veristand-cds
This repository contains the functionality used by our Jenkins server to build the NI VeriStand Custom Devices.

Included are a pipeline, defined [here](https://github.com/ni-veristand-cds/commonbuild/blob/master/src/ni/vsbuild/Pipeline.groovy), and other common scripts used during the build.

## Usage
Two files are required in order to use this pipeline, a `Jenkinsfile` and a `build.toml` file. Additionally, the pipeline assumes each executor node on the Jenkins server is tagged with certain labels.

The [LabVIEW Development System](http:/ni.com/labview) and the [LabVIEW Command Line Interface](https://github.com/JamesMc86/LabVIEW-CLI/releases) (CLI) are required on the build machine to use the LabVIEW build steps. The CLI can be installed by double-clicking the .vip file with [VI Package Manager](https://vipm.jki.net/) (VIPM) installed or directly through the VIPM application.

### Node Labels
Each node capable of building a custom device must have the label *'veristand'* and a label for each version of LabVIEW/VeriStand installed.
A node that is capable of building a custom device for VeriStand 2016 and 2017 would have the labels `veristand, 2016, 2017`.

### Jenkinsfile
The pipeline is used by a `Jenkinsfile` defined in other repositories in this organization.

The Jenkins server must be configured to load this library implicitly, either by the Jenkins Pipeline Global Library or as a Shared Pipeline Library within a job folder. To build a custom device for LabVIEW/VeriStand 2016 and 2017 if the library name configured in Jenkins is `vs-common-build`:

```groovy
// Jenkinsfile
@Library('vs-common-build') _
List<String> lvVersions = ['2016', '2017']
ni.vsbuild.PipelineExecutor.execute(this, lvVersions)
```

#### Dependencies
Some custom devices require builds of multiple repositories. This system allows a Jenkinsfile for one repository to specify a dependency on other repositories. Dependencies will be built before the pipeline for the top-level repository. Dependencies are an optional parameter to the `PipelineExecutor.execute()` method:

```groovy
// Jenkinsfile snippet
List<String> dependencies = ['dep1', 'dep2']
ni.vsbuild.PipelineExecutor.execute(this, lvVersions, dependencies)
```

### build.toml
The `build.toml` file defines the pipeline configuration and stages used during the build.

For a custom device that has only one LabVIEW project and simply needs to build every build spec in that project, the `build.toml` file will be fairly simple:

```
[archive]
build_output_dir = 'Built'
archive_location = 'C:\MyCustomDevice'

[projects.cd]
path = 'Source\MyCustomDevice.lvproj'

[[build.steps]]
name = 'Build My Custom Device'
type = 'lvBuildAll'
project = '{cd}'
```

Stages are ordered by the pipeline. Steps within the codegen and build stages are executed in the top to bottom order specified in `build.toml`. For a complete description of the available TOML configuration options, see the [build.toml specification](https://github.com/ni-veristand-cds/commonbuild/wiki/TOML-for-ni-veristand-cds).
