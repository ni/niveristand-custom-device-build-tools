from subprocess import check_call, CalledProcessError
from os import chdir
from pathlib import Path


def integrate_release(url, version, working_directory):
    """
    Integrates changes from the master branch of a git repository to a release branch named release/{version}.

    This can be called multiple times with the same url and working directory; in this case, the repository will
    not be cloned from scratch but will instead pull down changes to the existing repo.

    :param url: The upstream url to push the release branch to
    :param version: The version of the release, e.g. 19.0
    :param working_directory: The working directory to clone the repository to. If this directory already exists
        and is a git repository, the repository will be updated in place instead of cloning from scratch.
    """

    working_directory_path = Path(working_directory)
    print(working_directory_path)
    try:
        # Validate that a git repository exists at the requested location
        chdir(working_directory_path)
        check_call("git status")
    except (FileNotFoundError, CalledProcessError):
        # Directory does not exist, or is not a git repository
        chdir(working_directory_path.parent)
        check_call("git clone {0} \"{1}\"".format(url, working_directory_path.name))
        chdir(working_directory_path)

    check_call("git checkout master")
    check_call("git pull")
    check_call("git checkout -B release/{0}".format(version))
    check_call("git push -u origin release/{0}".format(version))


if __name__ == "__main__":
    import toml
    import sys

    file_contents = toml.load(sys.argv[1])
    for project in file_contents.values():
        integrate_release(project["url"], project["version"], project["directory"])
