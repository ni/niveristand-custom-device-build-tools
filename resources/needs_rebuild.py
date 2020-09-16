import json
import os
import sys

from os.path import exists, join


def validate_versions_exist(base_dir, versions):
    for version in versions_list:
        print(version)
        version_string = str(version)
        if not exists(join(base_dir, version_string)):
            return False
    return True


def validate_commits_match(base_dir, version):
    manifest_file = join(base_dir, str(version), 'installer', 'manifest.json')
    if not exists(manifest_file):
        return False

    with open(manifest_file) as f:
        data = json.load(f)

    previous_commit = data['scm']['GIT_COMMIT']
    if previous_commit.lower() != latest_commit.lower():
        return False

    return True


def should_rebuild(archive_dir, commit, versions):
    if not validate_versions_exist(archive_dir, versions):
        return True

    # just choose an arbitrary version because commit is the same
    # for all versions
    if not validate_commits_match(archive_dir, versions[0]):
        return True

    return False


if __name__ == "__main__":
    archive_dir = sys.argv[1]
    latest_commit = sys.argv[2]
    versions = sys.argv[3]

    versions_list = json.loads(versions)

    print(should_rebuild(archive_dir, latest_commit, versions_list))
