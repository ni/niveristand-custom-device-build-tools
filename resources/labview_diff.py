import os
import re
import shutil
import stat
import subprocess
import tempfile
import traceback
from contextlib import contextmanager
from os import path


def diff_vi(old_vi, new_vi, output_dir, workspace, lv_version):
    """
    Generates a diff of LabVIEW VIs

    :param old_vi: The older version of the VI; if bool(vi1) is false, the VI is assumed to be newly added
    :param new_vi: The newer version of the VI
    :param output_dir: The directory in which to store output
    :param workspace: The directory above the niveristand-custom-device-build-tools
    :param lv_version: The year version of LabVIEW to use for diffing
    """

    version_path = labview_path_from_year(lv_version)

    command_args = [
        "LabVIEWCLI.exe",
        "-LabVIEWPath", version_path,
        "-AdditionalOperationDirectory", workspace + r"\niveristand-custom-device-build-tools\lv\operations\\",
        "-OperationName", "DiffVI",
        "-NewVI", new_vi,
        "-OutputDir", output_dir,
    ]

    if old_vi:
        command_args.extend(["-OldVI", old_vi])

    subprocess.call(["taskkill", "/IM", "labview.exe", "/F"])
    subprocess.check_call(command_args)


def uniquify_fully_qualified_name(vi, output_dir, workspace, lv_version):
    """
    Renames a VI or its owning library such that it has a different fully qualified name.

    This is useful for ensuring LabVIEW can diff files properly.

    :param vi: The file you want to diff
    :param output_dir: The directory in which to store output
    :param workspace: The directory above the niveristand-custom-device-build-tools
    :param lv_version: The year version of LabVIEW to use for diffing
    """

    version_path = labview_path_from_year(lv_version)

    command_args = [
        "LabVIEWCLI.exe",
        "-LabVIEWPath", version_path,
        "-AdditionalOperationDirectory", workspace + r"\niveristand-custom-device-build-tools\lv\operations\\",
        "-OperationName", "UniquifyFullyQualifiedVIName",
        "-VI", vi,
    ]

    subprocess.call(["taskkill", "/IM", "labview.exe", "/F"])
    output = subprocess.check_output(command_args).decode("utf-8")
    match = re.search(r"^Unique Fully Qualified VI Name: (.+)$", output, re.MULTILINE)

    if not match:
        raise ValueError("Could not find VI name in output")
    return match.group(1).strip()


def labview_path_from_year(year):
    env_key = "labviewPath_" + str(year)
    if env_key in os.environ:
        return os.environ[env_key]

    return r"{0}\National Instruments\LabVIEW {1}\LabVIEW.exe".format(os.environ["ProgramFiles(x86)"], year)


def export_repo(target_ref):
    """
    Export a copy of the repository at a given ref to a temporary directory.

    :param target_ref: The ref you want to export, e.g. `origin\master`
    :return: A temporaryfile.TemporaryDirectory containing the repository at the given ref
    """

    directory = tempfile.TemporaryDirectory()
    shutil.copytree(".git", path.join(directory.name, ".git"))
    subprocess.check_call(["git", "checkout", "-f", target_ref], cwd=directory.name)

    @contextmanager
    def cleanup_make_all_writeable(directory):
        try:
            yield directory
        finally:
            # tempfile.TemporaryDirectory has a bug where it fails when readonly files
            # are present. Clearing the readonly flag manually fixes this.
            # When https://bugs.python.org/issue26660 is fixed, this code can be removed,
            # and the temporary directory can be returned directly
            for root, dirs, files in os.walk(directory.name):
                for file in files:
                    os.chmod(root + "/" + file, stat.S_IWRITE)

    return cleanup_make_all_writeable(directory)


def get_changed_labview_files(target_ref):
    """
    Get LabVIEW files which have changed compared to the target ref.

    :param target_ref: The git ref to check for changed files against
    :return: Tuples of the form (status, filename) where status is either "A" or "M", depending on whether the file was added or modified.
    """
    diff_args = ["git", "diff", "--name-status", "--diff-filter=AM", target_ref + "...", "HEAD"]
    diff_output = subprocess.check_output(diff_args).decode("utf-8")

    # https://regex101.com/r/EFVDVV/1
    diff_regex = re.compile(r"^([AM])\s+(.*\.vi[tm]?)$", re.MULTILINE)

    for match in re.finditer(diff_regex, diff_output):
        yield match.group(1), match.group(2)


def diff_repo(workspace, output_dir, target_branch, lv_version):
    """
    Generate image diffs for LabVIEW VIs in a Git repo.

    VIs which fail to be diffed are logged to {output_dir}/diff_failures.txt.
    """
    diffs = get_changed_labview_files(target_branch)

    with export_repo(target_branch) as directory:
        for status, filename in diffs:
            try:
                if status == "A":
                    print("Diffing added file: " + filename)
                    diff_vi(None, path.abspath(filename), path.abspath(output_dir), workspace, lv_version)
                elif status == "M":
                    print("Diffing modified file: " + filename)
                    # LabVIEW won't let us load two files with the same name into memory,
                    # so we need to ensure the files have unique fully-qualified names.
                    old_file = path.join(directory.name, filename)
                    old_file_unique = uniquify_fully_qualified_name(path.abspath(old_file), path.abspath(output_dir), workspace, lv_version)
                    diff_vi(old_file_unique, path.abspath(filename), path.abspath(output_dir), workspace, lv_version)
                else:
                    print("Unknown file status: " + filename)
            except:
                print("Failed to diff \"{0}\".".format(filename))
                traceback.print_exc()
                with open(output_dir + "\\diff_failures.txt", "a+") as file:
                    file.write(filename + "\n")
            finally:
                subprocess.check_call(["git", "reset", "--hard"], cwd=directory.name)


if __name__ == "__main__":
    import argparse

    parser = argparse.ArgumentParser(formatter_class=argparse.ArgumentDefaultsHelpFormatter)
    parser.add_argument(
        "workspace",
        help="Directory which contains the `niveristand-custom-device-build-tools` repository "
             "(as opposed to the `niveristand-custom-device-build-tools` repository itself)"
    )
    parser.add_argument(
        "output_dir",
        help="Directory in which to generate output"
    )
    parser.add_argument(
        "labview_version",
        help="Year version of LabVIEW you wish to use for diffing"
    )
    parser.add_argument(
        "--target",
        help="Target branch or ref the diff is being generated against",
        default="origin/master"
    )

    args = parser.parse_args()

    diff_repo(args.workspace, args.output_dir, args.target, args.labview_version)
