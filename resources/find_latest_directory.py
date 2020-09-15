import glob
import os
import sys

from os.path import exists, getmtime, join

archive_dir = sys.argv[1]
print(archive_dir)

sub_dirs = glob.glob(join(archive_dir, '*'))
if not sub_dirs:
    print(None)
print(max(sub_dirs, key=getmtime))
