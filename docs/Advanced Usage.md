# Advanced Usage

## Overriding dependencies to use existing artifacts

By default dependencies are always rebuilt. If the branch name being built on the top-level repository also exists on the dependency, that branch will be used to build the dependency; if no such branch exists, the `master` branch is used.

This is not always the desired behavior. For a variety of reasons (e.g. the branch names do not match, the branches are part of pull requests which override the branch name in Jenkins, debugging with fixed dependencies), it may be beneficial to use an existing build of the dependencies, instead of rebuilding from source.

The build tools support specifying a path to use for a given dependency's built artifacts as an environment variable. These environment variables can be easily set in a Jenkinsfile, either directly in the repository or when replaying a previous build. The environment variable should be the dependency name suffixed with **`_DEP_DIR`**, and should refer to a path to a previous build's output.

In the example below, `dependency-A` would use an existing built artifact, while `dependency-B` would be rebuilt as usual.

```groovy
@Library('vs-build-tools') _

def lvVersions = ['2016', '2017', '2018', '2019']

List<String> dependencies = ['dependency-A', 'dependency-B']

// Force dependency-A to use an existing artifact, instead of being rebuilt.
env.'dependency-A_DEP_DIR' = '\\\\somenetworkshare\\dependency_A\\ni\\export\\master\\Build 1'

ni.vsbuild.PipelineExecutor.execute(this, 'veristand', lvVersions, dependencies)
```

