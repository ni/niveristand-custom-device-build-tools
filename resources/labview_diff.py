import os
import subprocess
import sys


def labview_diff(vi1, vi2, working_dir, lv_version):
    """
    Generates a diff of LabVIEW VIs

    VIs which fail to be diffed are logged to `working_dir\diff_failures.txt`.

    :param vi1: The first VI to diff, typically $LOCAL
    :param vi2: The second VI to diff, typically $REMOTE
    :param working_dir: The directory in which to store output
    :param lv_version: The year version of LabVIEW to use for diffing
    """
    if vi1[-3:] != ".vi" and vi1 != r"\\.\nul":
        return

    if vi2[-3:] != ".vi":
        return

    version_path = labview_path_from_year(lv_version)
    workspace = os.environ["WORKSPACE"]

    command_args = [
        "LabVIEWCLI.exe",
        "-LabVIEWPath", version_path,
        "-AdditionalOperationDirectory", workspace + r"\niveristand-custom-device-build-tools\lv\operations\\",
        "-OperationName", "DiffVI",
        "-OldVI", vi1,
        "-NewVI", vi2,
        "-OutputDir", working_dir,
    ]

    subprocess.call(["taskkill", "/IM", "labview.exe", "/F"])
    try:
        subprocess.check_call(command_args)
    except subprocess.CalledProcessError:
        import traceback
        print("Failed to diff {0} and {1}.".format(vi1, vi2))
        traceback.print_exc()

        with open(working_dir + "\\diff_failures.txt", "a+") as file:
            file.write(vi1 + "\n")


def labview_path_from_year(year):
    env_key = "labviewPath_" + str(year)
    if env_key in os.environ:
        return os.environ[env_key]

    return r"C:\Program Files (x86)\National Instruments\LabVIEW {0}\LabVIEW.exe".format(year)


if __name__ == "__main__":
    labview_diff(sys.argv[1], sys.argv[2], sys.argv[3], sys.argv[4])
