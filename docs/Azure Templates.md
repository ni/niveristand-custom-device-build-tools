# stages.yml

stages.yml contains definitions for the parameters that are required or optional for custom device builds, as well as a definition of the stages and jobs that will be run.  See the table at the bottom of [Azure Pipeline YAML](Azure%20Pipeline%20YAML.md) for more details on the parameters that are accepted by the templates.

Each run consists of 3 stages: a DiffVI stage, a Build stage, and a Verification stage.  These stages are outlined below.

## Stage 1: DiffVIs
DiffVIs is a stage that will comment with LabVIEW diff images to Pull Requests on GitHub.  It is only used on Pull Requests and requires a secret token to be approved before it will run.  If a build other than a Pull Request is run, this stage is skipped.

The LabVIEW diff images will be generated using the last specified 64-bit version of LabVIEW in the list of `lvVersionsToBuild`.

## Stage 2: Build
This is the main stage of the pipeline, which builds the `buildSteps` and `packages` defined in the pipeline and archives the build outputs in the `archiveLocation`.  It runs one job for each sequence in `lvVersionsToBuild`, and each job will consist of:

### steps-pre-build.yml
a single instance of the `steps-pre-build.yml` template to configure variables, checkout repos, clear the compiled cache, and run any pre-build codegen steps.

### steps-build.yml
For each sequence defined in `buildSteps`, it adds a separate instance of the `steps-build.yml` template, which will copy any `dependencies`, place .config files near LabVIEW projects, and then run the build specifications defined in the `buildStep`.

### steps-post-build.yml
After all `buildSteps` have been processed, a single instance of the `steps-post-build.yml` template packages each nipkg defined in `packages` and places the generated nipkgs and build outputs into the `archiveLocation`.

## Stage 3: Finalization
Last is a finalization stage that marks the build as `.finished` if everything previous was run successfully.

