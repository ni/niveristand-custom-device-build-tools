from subprocess import check_call, CalledProcessError
from os import chdir, getcwd
from re import match
from pathlib import Path


def integrate_release(url, version, working_directory):
    """
    Integrates changes from the main branch of a git repository to a release branch named release/{version}.

    This can be called multiple times with the same url and working directory; in this case, the repository will
    not be cloned from scratch but will instead pull down changes to the existing repo.

    :param url: The upstream url to push the release branch to
    :param version: The version of the release, e.g. 19.0
    :param working_directory: The working directory to clone the repository to. If this directory already exists
        and is a git repository, the repository will be updated in place instead of cloning from scratch.
    """

    assert url.endswith(".git"), 'Expected url to end with ".git"'
    assert match(
        r"^\d+\.\d+(\.\d+)?$", version
    ), 'Version must be in format "x.y" or "x.y.z"'  # https://regex101.com/r/xqyo5X/2
    working_directory_path = Path(working_directory)

    previous_working_directory = getcwd()
    try:
        try:
            # Validate that a git repository exists at the requested location
            chdir(working_directory_path)
            check_call("git status")
        except (FileNotFoundError, CalledProcessError):
            # Directory does not exist, or is not a git repository
            chdir(working_directory_path.parent)
            clone_command = 'git clone {0} "{1}"'.format(url, working_directory_path.name)
            check_call(clone_command)
            chdir(working_directory_path)

        destination_branch = "release/{0}".format(version)

        check_call("git checkout main")
        check_call("git pull")
        check_call("git checkout -B " + destination_branch)
        check_call("git push -u origin " + destination_branch)
    finally:
        chdir(previous_working_directory)


if __name__ == "__main__":
    import toml
    import sys

    file_contents = toml.load(sys.argv[1])
    for name, project in file_contents.items():
        print()
        print("*" * 80)
        print("Merging release branch '{0}' for {1}".format(project["version"], name))
        print("*" * 80)
        integrate_release(project["url"], project["version"], project["directory"])
        print()
