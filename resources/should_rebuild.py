import glob
import json
import os
import sys

from os.path import exists, getmtime, join

archive_dir = sys.argv[1]
latest_commit = sys.argv[2]
versions = sys.argv[3:len(sys.argv)]

def find_latest_directory(base_path):
    """
    Returns the directory in base path that was last modified.
    
    :param base_path: The path to search.
    :return The directory that was last modified.
    """

    sub_dirs = glob.glob(join(base_path, '*'))
    if not sub_dirs:
        return None
    return max(sub_dirs, key=getmtime)


def validate_versions_exist(base_dir, versions):
    for version in versions:
        if not exists(join(base_dir, version)):
            trigger_rebuild()


def validate_commits_match(base_dir, version):
    manifest_file = join(base_dir, version, 'installer', 'manifest.json')
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


final_dir = find_latest_directory(archive_dir)
validate_versions_exist(final_dir, versions)

# just choose an arbitrary version because commit is the same
# for all versions
validate_commits_match(final_dir, versions[0])

print(False)
