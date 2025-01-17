# Define the coordinates of the area (x, y, width, height)
X_COORDINATE = 150
Y_COORDINATE = 80
WIDTH = 280
HEIGHT = 185


def detect_trash_in_area(processed_image):
    """
    Detects trash (pixels with value 255) within a specified area of the image.

    @param processed_image: The processed image where trash detection will be performed.
    @return: True if trash is detected within the specified area, False otherwise.
    """
    for row in range(Y_COORDINATE, Y_COORDINATE + HEIGHT):
        for col in range(X_COORDINATE, X_COORDINATE + WIDTH):
            valor_pixel = processed_image[row, col]
            # 255 represents the color white
            if valor_pixel == 255:
                #print('Trash detected')
                return True
    return False

