import argparse
from pathlib import Path
import shutil

if __name__ == '__main__':
    parser = argparse.ArgumentParser(description='Configure paths.')
    parser.add_argument('path', type=str, nargs=1,
                        help='path of the file "paths.txt"')
    args = parser.parse_args()
    # assert len(args) == 1
    path = Path(args.path[0]).absolute()
    root = Path(__file__).parent.absolute()/'paths.txt'
    shutil.copyfile(path, root)
    print('The paths have been configuired.')
        
    pass