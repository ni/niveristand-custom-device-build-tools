import json
import os
import sys
import toml
from os.path import split, splitext

file = sys.argv[1]
parsed_toml = toml.load(file)

base_path, file_name = os.path.split(file)
json_file_name = os.path.splitext(file_name)[0] + '.json'


print("Writing build description to", json_file_name)
with open(json_file_name, 'w') as outfile:
   json.dump(parsed_toml, outfile, indent=3)
