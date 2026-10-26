Guidelines for generating an azure-pipelines.yml file in an niveristand-custom-device:

Always include the following at the top of the azure-pipelines.yml:

```
trigger:
  batch: true
  branches:
    include:
      - main
      - release/*

resources:
  repositories:
    - repository: niveristand-custom-device-build-tools
      type:       github
      ref:        main
      endpoint:   nivs-custom-devices
      name:       ni/niveristand-custom-device-build-tools
```

This will trigger the branch to run CI builds on `main` and on any `release` branches.  It will also include the build-tools yaml templates that will be used below.

Choose the stage template for the LabVIEW version family your custom device uses:

* Use `stages-legacy.yml` for legacy LabVIEW `2024`, `2025`, and `2026` builds:

```yaml
stages:
  - template: azure-templates/stages-legacy.yml@niveristand-custom-device-build-tools
    parameters:
      # Add the parameters required for your build here.
```

* Use `stages.yml` for the newer LabVIEW `2027` build:

```yaml
stages:
  - template: azure-templates/stages.yml@niveristand-custom-device-build-tools
    parameters:
      # Add the parameters required for your build here.
```

Replace the comment with the parameters for your custom device pipeline. See [Azure Templates](./Azure%20Templates.md) for the stages and behavior of each template. The currently available parameters are listed below. Examples of parameter usage can be found in this repository's [azure-pipelines.yml](../azure-pipelines.yml).

| Parameter Name | Required? | Type | Supported Values |
| --- | --- | --- | --- |
| `stageName` | No | String | Stage name prefix; defaults to `Build` |
| `dependsOn` | No | Sequence | Stage names this stage depends on; defaults to an empty sequence |
| `disableDiff` | No | Boolean | Set to `true` to disable LabVIEW diffs on pull requests; defaults to `false` |
| `enableVirtualPackages` | No | Boolean | Defaults to `false` |
| `silenceDependencyFailures` | No | Boolean | Defaults to `false` |
| `pool` | No | String | Agent pool name; defaults to `DevCentral-VeriStand-CustomDevices` |
| `lvVersionToDiff` | No | String | LabVIEW version for VI diffs; defaults to `2024` in `stages-legacy.yml` and `2027` in `stages.yml` |
| `lvVersionsToBuild` | No | Nested Sequence | 1 or more Named Sequences containing elements `version` and `bitness`; defaults are template-specific (`2024`–`2026` for legacy, `2027` for `stages.yml`) |
| →`version` | Yes<sup>1</sup> | String | '2024', '2025', '2026' or '2027' |
| →`bitness` | Yes<sup>1</sup> | String | '32bit', '64bit' |
| `codegenVis` | No | Sequence | 1 or more relative paths to VIs that need to be run before any `buildSteps` are performed |
| `dependencies` | No | Nested Sequence | 1 or more Named Sequences containing elements `source`, `file`, and `destination` defining dependencies that need to be copied to the specified destination before each `buildStep` |
| →`source` | Yes<sup>1</sup> | String | network archive path to the dependency |
| →`file` | Yes<sup>1</sup> | String | relative path to the dependency within the network archive path.  Can use `$target` as a variable to represent `Windows` or `Linux x64`, depending on the `target` defined in each `buildStep` |
| →`destination` | Yes<sup>1</sup> | String | relative path to the location where the specified dependency should be copied |
| `buildSteps` | No | Nested Sequence | 1 or more Named Sequences containing elements `projectLocation`, `buildOperation`, `target`, and `buildSpec`.  Each Named Sequence can optionally contain `exclusions` and/or `projectOverride` as well.
| →`projectLocation` | Yes<sup>1</sup> | String | Relative path to the project from which to run the build specification |
| →`buildOperation` | Yes<sup>1</sup> | String | 'ExecuteBuildSpec', 'ExecuteBuildSpecAllTargets', or 'ExecuteAllBuildSpecs'  
| →`target` | Yes<sup>1</sup> | String | Name of the Target in the LabVIEW Project.  If `buildOperation` is 'ExecuteBuildSpecAllTargets' or 'ExecuteAllBuildSpecs', this value is ignored, and all Targets with a matching Build Specification are attempted. |
| →`buildSpec` | Yes<sup>1</sup> | String | Name of the Build Specification to build in the LabVIEW Project.  If `buildOperation` is 'ExecuteAllBuildSpecs', this value is ignored, and all Build Specifications are attempted. |
| →`exclusions` | No | Nested Sequence | 1 or more Sequences containing elements `version` and `bitness`.  This should mirror 1 or more `lvVersionsToBuild` sequences that need to be skipped for the parent `buildStep` |
| →`projectOverride` | No | String | Include this parameter with a value of 'Absolute' if the `projectLocation` is an absolute path instead of a relative path
| `releaseVersion` | Yes | String | a version number with the format AA.B.C, where AA is the last two digits of the year, B is the quarter (0 = Q1, 3 = Q2, 5 = Q3, 8 = Q4), and C is used for patch releases |
| `buildOutputLocation` | Yes | String | the relative path to the outputs of the build |
| `archiveLocation` | Yes | String | a network path to the location where this build should be archived upon build completion |
| `submodules` | No | Boolean | Set to `true` to use submodules; defaults to `false` |
| `packages` | No | Nested Sequence | 1 or more Nested Sequences that contain a `controlFileName` and a Nested Sequence of `payloadMaps` |
| →`controlFileName` | Yes<sup>1</sup> | String | the name of a control file found at the top level of the repo |
| →`payloadMaps` | Yes<sup>1</sup> | Nested Sequence | 1 or more Named Sequences that contain a `payloadLocation` and an `installLocation` |
| →→`payloadLocation` | Yes<sup>1</sup> | String | relative path to the build outputs that need to be copied to the `installLocation` |
| →→`installLocation` | Yes<sup>1</sup> | String | relative path to the location where files need to be installed by the nipkg.  The variable `$(lvVersion)` can be used, which corresponds to the LabVIEW version used in the current `buildStep`, and the variable $(nipkgx64suffix) can be used to generate the suffix `64` for only 64-bit LabVIEW builds |

<sup>1. This is required, but only if its parent is defined</sup>