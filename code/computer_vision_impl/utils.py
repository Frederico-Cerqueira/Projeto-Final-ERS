import os

#INPUT_DIR_HALL = os.path.join('data', 'pi-images')
INPUT_DIR_HALL = os.path.join('data', 'test-images/hall')
INPUT_DIR_OUTSIDE = os.path.join('data', 'test-images/outside')
INPUT_DIR_LAB = os.path.join('data', 'test-images/lab')
OUTPUT_DIR = os.path.join('data', 'output')


def in_file(filename):
    """Returns the path of an input file"""
    return os.path.join(INPUT_DIR_HALL, filename)


def out_file(filename):
    """Returns the path of an output file"""

    return os.path.join(OUTPUT_DIR, filename)

