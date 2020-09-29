import glob
import os
import sys

from os.path import exists, getmtime, join


def find_latest_directory(base_path):
    sub_dirs = glob.glob(join(base_path, "*"))
    if not sub_dirs:
        return None
    return max(sub_dirs, key=getmtime)


if __name__ == "__main__":
    archive_dir = sys.argv[1]
    print(find_latest_directory(archive_dir))
