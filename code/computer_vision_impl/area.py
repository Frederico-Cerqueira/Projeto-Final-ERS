import os
from utils import in_file


def detect_trash_in_area(processed_image):
    # Define the coordinates of the area (x, y, width, height)
    x, y, w, h = 150, 80, 280, 185

    # Iterate over the pixels within the defined area and check the value of each pixel
    for row in range(y, y + h):
        for col in range(x, x + w):
            valor_pixel = processed_image[row, col]
            if valor_pixel == 255:
                # Trash detected!
                return True
    return False


if __name__ == '__main__':
    image_directory = os.path.join('data', 'pi-images', 'outside')
    image_filename = 'box3.jpg'
    image_path = os.path.join(image_directory, image_filename)
    detect_trash_in_area(in_file('teste.jpg'))
