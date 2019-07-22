# NI VeriStand Custom Device Build Tools
The **niveristand-custom-device-build-tools** repository provides a common set of tools to automate building NI VeriStand custom devices using the [Jenkins automation server](https://jenkins.io/). The intended audience includes custom device developers and integrators.

## Usage
Two files are required in order to use this pipeline for a given repository, a `Jenkinsfile` and a `build.toml` file. Additionally, the pipeline assumes each executor node on the Jenkins server is tagged with certain labels.

### Node Labels
Each node capable of building a custom device must have the label *'veristand'* and a label for each version of LabVIEW/VeriStand installed.
A node that is capable of building a custom device for VeriStand 2016 and 2017 would have the labels `veristand`, `2016`, and `2017`.

### Jenkinsfile
The pipeline is used by a `Jenkinsfile` defined in other repositories in this organization.

The Jenkins server must be configured to load this library implicitly, either by the Jenkins Pipeline Global Library or as a Shared Pipeline Library within a job folder. To build a custom device for LabVIEW/VeriStand 2016 and 2017 if the library name configured in Jenkins is `vs-build-tools`:

```groovy
// Jenkinsfile
@Library('vs-build-tools') _
List<String> lvVersions = ['2016', '2017']
ni.vsbuild.PipelineExecutor.execute(this, lvVersions)
```

#### Build-Time Dependencies
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

Stages are ordered by the pipeline. Steps within the codegen and build stages are executed in the top to bottom order specified in `build.toml`. For a complete description of the available TOML configuration options, see the [build.toml specification](docs/Toml%20Help.md).

## LabVIEW Version
The LabVIEW source for this repository is saved for LabVIEW 2014, but is forward compatible to newer versions.

## Dependencies
The following top-level dependencies are required on the build machine to use the repository:

- [LabVIEW Professional Development System](http:/ni.com/labview)
- [LabVIEW Command Line Interface](http://www.ni.com/en-us/support/downloads/software-products/download.ni-labview-command-line-interface.html)
- [Python](https://www.python.org/downloads/) (Version 3.6.3 or later)

## Git History & Rebasing Policy
Branch rebasing and other history modifications will be listed here, with several notable exceptions:
- Branches prefixed with `dev/` may be rebased, overwritten, or deleted at any time.
- Pull requests may be squashed on merge.

## License
The NI VeriStand Custom Device Testing Tools are licensed under an MIT-style license (see LICENSE). Other incorporated projects may be licensed under different licenses. All licenses allow for non-commercial and commercial use.
