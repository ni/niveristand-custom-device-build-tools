import glob
import json
import os
import sys

from os.path import exists, join

archive_dir = sys.argv[1]
latest_commit = sys.argv[2]
versions = sys.argv[3]

versions_list = json.loads(versions)


def validate_versions_exist(base_dir, versions):
    for version in versions_list:
        print(version)
        version_string = str(version)
        if not exists(join(base_dir, version_string)):
            trigger_rebuild()


def validate_commits_match(base_dir, version):
    manifest_file = join(base_dir, str(version), 'installer', 'manifest.json')
    if not exists(manifest_file):
        trigger_rebuild()

    with open(manifest_file) as f:
        data = json.load(f)

    previous_commit = data['scm']['GIT_COMMIT']
    if previous_commit.lower() != latest_commit.lower():
        trigger_rebuild()


def trigger_rebuild():
    print(True)
    sys.exit(0)


validate_versions_exist(archive_dir, versions_list)

# just choose an arbitrary version because commit is the same
# for all versions
validate_commits_match(archive_dir, versions_list[0])

print(False)
