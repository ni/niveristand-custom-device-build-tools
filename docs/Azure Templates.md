# Azure stage templates

The build-tools repository provides two stage templates. Use `stages-legacy.yml` for the legacy LabVIEW `2024`, `2025`, and `2026` builds, and use `stages.yml` for the newer LabVIEW `2027` build. Both templates accept the parameters listed in [Azure Pipeline YAML](Azure%20Pipeline%20YAML.md) and follow the same overall stage structure.

Each run has two pipeline stages: DiffVIs and Build. The Build stage contains the build jobs, a packaging job, and a finalization job.

## Pipeline stage 1: DiffVIs

DiffVIs generates LabVIEW diff images and comments with them on GitHub pull requests. This stage runs only for pull requests, when diffs have not been disabled, and requires an approved secret token. It uses the LabVIEW version specified by `lvVersionToDiff`. The templates default this to `2024` in `stages-legacy.yml` and `2027` in `stages.yml`.

## Pipeline stage 2: Build

### Build jobs

The Build stage runs one build job for each entry in `lvVersionsToBuild`. Each job prepares the environment, checks out repositories, clears the compiled cache, and runs any configured code-generation VIs using `steps-prepare.yml`. It then runs the configured `buildSteps` using `steps-build.yml`, copies dependencies as needed, closes LabVIEW, and archives the build outputs.

### Packaging job

After the build jobs complete, the `Job_Package_All` job packages the configured `packages` with `steps-packaging.yml` and places the generated packages and build outputs in `archiveLocation`.

### Finalization job

The `Finalize` job runs only after successful packaging. It validates package contents against the last successful build in `main` and adds a `.finished` file to the archive location. The legacy template additionally requires its final-validation agent to have the LabVIEW version specified by `lvVersionToDiff`.

## `stages-legacy.yml`

Use `stages-legacy.yml` for the legacy `2024`–`2026` LabVIEW builds. Its default `lvVersionsToBuild` includes 64-bit LabVIEW `2024`, `2025`, and `2026`.

## `stages.yml`

Use `stages.yml` for the newer `2027` LabVIEW build. Its default `lvVersionsToBuild` includes 64-bit LabVIEW `2027`.

Both templates use the same DiffVIs and Build stage structure described above. Their default LabVIEW versions and the legacy final-validation agent demand differ; set `lvVersionsToBuild` and `lvVersionToDiff` explicitly if you need to override the defaults.
