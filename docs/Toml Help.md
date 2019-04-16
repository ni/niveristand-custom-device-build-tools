**Note:** A high-level understanding of TOML is recommended. For more information, see [TOML](https://github.com/toml-lang/toml).

While the pipeline entry point is defined in the `Jenkinsfile`, the actual substance of a VeriStand custom device build is defined in `build.toml`. This document describes the available configuration options in `build.toml` and how those options impact the actual build.

There are two types of configuration captured in the build file:
1. Non-stage configuration
2. Stage configuration

**Table of Contents**
* [Non-Stage Configuration](#non-stage-configuration)
   * [Projects](#projects)
   * [Dependencies](#dependencies)
* [Stages](#stages)
   * [Steps](#steps)
      * [lvRunVi](#lvrunvi)
      * [lvBuildSpec](#lvbuildspec)
      * [lvBuildSpecAllTargets](#lvbuildspecalltargets)
      * [lvBuildAll](#lvbuildall)
      * [lvSetConditionalDisableSymbol](#lvsetconditionaldisablesymbol)
   * [Codegen](#codegen)
   * [Build](#build)
   * [Package](#package)
   * [Test](#test)
   * [Archive](#archive)

# Non-Stage Configuration
## Projects
Projects is a list of LabVIEW projects containing build specs that will be used during the build.

### Definition
To define a new project, include a new table in the projects table.

`[projects.newproject]`

#### Supported Keys
Key | Description | Required
-|-|-
`path`| location of the project relative to the Jenkins build workspace | **YES**

#### Example
```
[projects.first]
path = 'src\first.lvproj'

[projects.second]
path = 'src\support\second.lvproj'
```

### Notes
If at least one project is included, the codegen stage will be added to the pipeline. During codegen, each project will get a `.config` file that defines the VeriStand .Net assembly versions required to build the project for the associated VeriStand version. This keeps build information version-agnostic.

Projects can also be referenced by codegen and build steps as described below.

## Dependencies
Dependencies is a list of other repositories containing libraries that are needed to build the current repository. It is assumed that each dependency has been built prior to the beginning of the pipeline for the current repository. This is accomplished by specifying the `dependencies` variable in the `Jenkinsfile` as described in the [readme](/README.md).

### Definition
To define a new dependency, include a new table in the dependencies table. The new table name should match the name of the required repository. For example, if there exists repository ni-veristand-cds/dependency-repository, the definition would look like:

`[dependencies.dependency-repository]`

#### Supported Keys
Key | Description | Required
-|-|-
`libraries`| array of libraries to be copied from the dependency | **YES**
`copy_location`| location to copy each item in `libraries` relative to the Jenkins build workspace | **YES**

#### Example
```
[depenencies.my-first-repo]
libraries = ['first.lvlibp']
copy_location = 'src'

[dependencies.my-second-repo]
libraries = ['second.llb', 'second.dll']
copy_location = 'src\deps'
```

### Notes
If the dependency has multiple versions of a library (for instance a different copy for each supported OS), the correct version can be chosen by using the `dependency_target` option for codegen and build steps as described below.

# Stages

The available stages of the build and their order are defined in the main [pipeline](/src/ni/vsbuild/Pipeline.groovy). The Setup and Checkout stages are always included, but any other stages required for the build must be configured in `build.toml`. The order in which stages appear in the configuration does not change the order in which they occur during the build.

## Steps
Some stages may require multiple steps in order to complete. For example, multiple VIs may need to run during code generation or a full build may require building multiple build specs in different projects. This is accomplished by creating a **_steps_** array for a given stage. Steps are executed in the order they appear in `build.toml` and are used by the **Codegen** and **Build** stages.

Each step requires a `name` and `type` key to be defined. `type` is the name of the supported step type that should execute

Key | Description | Required
-|-|-
`name` | name of the step within the stage | **YES**
`type` | string matching the type of step to execute | **YES**

### Definition
To define a new step, a new item must be added to the **_steps_** array for the required stage.

`[[stage.steps]]`

#### Supported Steps
##### lvRunVi
The LvRunVi step executes a VI at the provided path relative to the Jenkins workspace.

###### Supported Keys
Key | Description | Required
-|-|-
`vi` | path to the VI to execute | **YES**

###### Example
```
[[codegen.steps]]
name = 'Generate Source'
type = 'lvRunVi'
vi = 'src\writeScript.vi'
```

###### Notes
The lvRunVi step does not accept inputs or return values. It is intended to act like a void function with no parameters. All functionality must be self-contained within the VI or its subVIs.

---

##### lvBuildSpec
The LvBuildSpec step builds a single build spec under a specific target in the provided project.

###### Supported Keys
Key | Description | Required
-|-|-
`project` | reference to the project table in the projects section of `build.toml`  | **YES**
`target` | name of the target that owns the `build_spec` | **YES**
`build_spec` | name of the specification to be built | **YES**
`dependency_target` | path prepended to a dependency's `libraries` key to fetch the correct version of a dependency | **NO**
`output_dir` | path prepended to a dependency's `libraries` key | **NO** (**_deprecated_**)
`output_libraries` | array of libraries to be copied to the `output_dir` | **NO** (**_deprecated_**)

*If `output_librarires` is defined, `output_dir` must also be defined, and vice versa.

###### Example
Execute a build spec with no dependencies and no alteration of the output directory.
```
[[build.steps]]
name = 'Configuration Library'
type = 'lvBuildSpec'
project = '{first}' # uses the value 'src\first.lvproj' from the example in the Projects section of this document
target = 'My Computer'
build_spec = 'Configuration Release'
```

Execute a build spec and require the 64-bit Linux dependency libraries.
```
[[build.steps]]
name = 'Linux x64 Library'
type = 'lvBuildSpec'
project = '{linux64}' # assume there was a 'projects.linux64' item created
target = 'Linux 64'
build_spec = 'Engine Release'
dependency_target = 'linux64' # get dependencies from linux64 directory
```

(**_Deprecated_**) Execute a build spec and output the specified libraries to a specific sub-directory. Builds spec My Library and copies output.llb to a pharlap directory instead of leaving it where it was built.
```
[[build.steps]]
name = 'Pharlap Library'
type = 'lvBuildSpec'
project = '{pharlap}' # assume there was a 'projects.pharlap' item created
target = 'PXI Pharlap'
build_spec = 'My Library'
output_libraries = ['output.llb']
output_dir = 'pharlap'
```

Instead of using this method, the build spec should be responsible for putting libraries in the correct location. This can be accomplished by making the Target destination directory in the build spec look like `pharlap:\output.llb`. The resulting build will be placed in a _pharlap_ directory, so there is no need to specify where the output should go.

---

##### lvBuildSpecAllTargets
The LvBuildSpecAllTargets step builds all builds specs of the specified name in the provided project.

###### Supported Keys
Key | Description | Required
-|-|-
`project` | reference to the project table in the projects section of `build.toml`  | **YES**
`build_spec` | name of the specification to be built | **YES**
`dependency_target` | path prepended to a dependency's `libraries` key to fetch the correct version of a dependency | **NO**


###### Example
Execute a build spec with no dependencies.
```
[[build.steps]]
name = 'Engine Libraries'
type = 'lvBuildSpecAllTargets'
project = '{first}' # uses the value 'src\first.lvproj' from the example in the Projects section of this document
build_spec = 'Engine Release'
```

Execute a build spec and require the 64-bit Linux dependency libraries.
This is a little weird because there shouldn't be multiple build specs of the same name if specific dependencies are required, but it still works. Using lvBuildSpec would be more concise.
```
[[build.steps]]
name = 'Linux x64 Libraries'
type = 'lvBuildSpecAllTargets'
project = '{linux64}' # assume there was a 'projects.linux64' item created
build_spec = 'Linux 64 Release'
dependency_target = 'linux64' # get dependencies from linux64 directory
```

---

##### lvBuildAll
The LvBuildAll step builds all build specs under all targets in the specified project.

###### Supported Keys
Key | Description | Required
-|-|-
`project` | reference to the project table in the projects section of `build.toml`  | **YES**

###### Example
```
[[build.steps]]
name = 'My Custom Device'
type = 'lvBuildAll'
project = '{first}' # uses the value 'src\first.lvproj' from the example in the Projects section of this document
```

---

##### lvSetConditionalDisableSymbol
The LvSetConditionalDisableSymbol step sets a conditional disable symbol defined in the specified project to the desired value.This would typically be used during codegen to configure a project's source prior to building so only the useful code is included.

###### Supported Keys
Key | Description | Required
-|-|-
`project` | reference to the project table in the projects section of `build.toml`  | **YES**
`symbol` | symbol to be set  | **YES**
`condition` | expression to determine which value is chosen for `symbol | **YES**
`true_value` | desired value of `symbol` when `condition` evaluates to TRUE | **YES**
`false_value` | desired value of `symbol` when `condition` evaluates to FALSE | **YES**

###### Example
```
[[codegen.steps]]
name = 'Set Conditional Disable''
type = 'lvSetConditionalDisableSymbol'
project = '{first}' # uses the value 'src\first.lvproj' from the example in the Projects section of this document
symbol = 'My Symbol'
condition = 'lvVersion < 2017'
true_value = '2017 or later'
false_value = '2016 or earlier'
```

###### Notes
The symbol must already exist in the project. This step will not create a new disable symbol.

`condition` may only use comparison with `lvVersion` currently. The supported operators are `<` `<=` `>` `>=` `==`, and `!=`.

---

## Codegen
The codegen stage is the first user-defined stage to execute. It is intended to execute steps that prepare the source prior to building.

### Definition
To define steps to execute during codegen, include a steps array in the codegen table. Valid steps are defined above.

`[[codegen.steps]]`

#### Notes
As mentioned above, if `projects` has any items, the codegen stage will be included in the pipeline to generate `.config` files for each project. After the `.config` files are created, any steps defined in the `[codegen.steps]]` array will execute.

## Build
The build stage executes immediately after codegen. If there are no items in the `[[build.steps]]` array, this stage will not be added to the pipeline.

### Definition
To define steps to execute during build, include a steps array in the build table. Valid steps are defined above.

`[[build.steps]]`

### Notes
Most often, this stage will contain one or more of the lvBuild* steps. If there are no steps in `[[build.steps]]`, this stage will not be added to the pipeline.

## Test
The test stage executes immediately after the build stage. It runs any tests specified in the build.toml.

### Definition
To define steps to execute during build, include a steps array in the test table. Valid steps are defined above.

`[[test.steps]]`

### Notes
Most often, this stage will contain one or more of the test steps. If there are no steps in `[[test.steps]]`, this stage will not be added to the pipeline.

#### Supported Testers
The test stage requires a `type` to be defined.

Key | Description | Required
-|-|-
`type`| test framework to use | **YES**

##### lvVITester
The VI Tester type requires the [JKI VI Tester](https://github.com/JKISoftware/JKI-VI-Tester) and the [niveristand-custom-device-testing-tools](https://github.com/ni/niveristand-custom-device-testing-tools) to run tests.

###### Supported Keys
Key | Description | Required
-|-|-
`test_path`| path to the test to execute | **YES**

###### Example
```
[[test.steps]]
name = 'Functional_Test'
type = 'lvVITester'
test_path='\Source\Tests\Unit\UnitTestCase.lvclass'
```

##### Notes
In order to use the VI Tester type, the [niveristand-custom-device-testing-tools](https://github.com/ni/niveristand-custom-device-testing-tools) must be forked to the organization that contains the repository running through the pipeline. While developing with the testing tools, the [niveristand-custom-device-testing-tools](https://github.com/ni/niveristand-custom-device-testing-tools) repository must be cloned to a directory that is adjacent to the development directory.

## Package
The package stage executes immediately after the build stage. It takes the specified directory and packages everything into a single file for simple distribution.

### Definition
To define a single package stage, add a package table to `build.toml`.

`[package]`

To export multiple package stages, add an array of packages to `build.toml`.

`[[package]]`

`[[package]]`

#### Supported Packages
The package stage requires a `type` to be defined.

Key | Description | Required
-|-|-
`type`| file extension of the final package | **YES**

##### nipkg
The nipkg package type builds a package that can be installed through NI Package Manager (NIPM). NIPM must be installed on the build machine in order to build this type.

###### Supported Keys
Key | Description | Required
-|-|-
`payload_dir` | location of files to be packaged | **YES**
`install_destination` | location where packaged files should be installed | **YES**
`<lvVersion>_install_destination` | location where packaged files should be installed if building with the supplied lvVersion | **NO**

###### Example
```
[package]
type = 'nipkg'
payload_dir = 'Built'
install_destination = 'documents\National Instruments\NI VeriStand {veristand_version}\Custom Devices'
2016_install_destination = 'documents\\National Instruments\\NI VeriStand {veristand_version}\\Custom Devices\\SLSC Plug-ins'
```

---

##### zip

The zip package type builds a compressed package that can be installed using ZIP decompression utilities. Most operating systems natively support inflating a compressed ZIP file.

###### Supported Keys
Key | Description | Required
-|-|-
`payload_dir` | location of files to be compressed and packaged | **YES**

###### Example
```
[[package]]
type = 'zip'
payload_dir = 'Source'
```

### Notes
If no package stage is defined in `build.toml` this stage will not be added to the pipeline. If a package stage is defined, an archive stage must also be defined.
---

## Archive
The archive stage executes immediately after the package stage. During this stage, the output of the build and package stages is copied to a more permanent location so it can be consumed later.

### Definition
To define an archive stage, add an archive table to `build.toml`.

`[archive]`

#### Supported Keys
Key | Description | Required
-|-|-
`build_output_dir`| top-level directory containing all build output | **YES**
`archive_location`| absolute path to location where `build_output_dir` will be copied* | **YES**

*If any __Build__ step contains an `output_libraries` key, only the output libraries will be copied to `archive_location`.

#### Example
```
[archive]
build_output_dir = 'Built'
archive_location = 'C:\MyCustomDevice'
```

### Notes
If any **Build** step contains an `output_libraries` key, an archive stage must exist.
