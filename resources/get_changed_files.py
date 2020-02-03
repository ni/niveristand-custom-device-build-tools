import git_utilities
import json


def main(target):
    output = [filename for _, filename in git_utilities.get_changed_files(args.target)]
    print(json.dumps(output))


if __name__ == "__main__":
    import argparse
    
    parser = argparse.ArgumentParser(formatter_class=argparse.ArgumentDefaultsHelpFormatter)
    parser.add_argument(
        "--target",
        help="Target branch or ref to generate a list of changed files against",
        default="origin/master"
    )

    args = parser.parse_args()

    main(args.target)
