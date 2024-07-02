import cv2
import numpy as np


def image_processing(img):
    """
    Processes the image to isolate an object from the background by applying binarization and morphological operations.

    @param img: Input image in RGB format.

    @return Processed image after morphological operations, where the object is white and the background is black.
    """

    image = cv2.resize(img, None, fx=0.25, fy=0.25)
    hsv_image = cv2.cvtColor(image, cv2.COLOR_BGR2HSV)
    h_channel = hsv_image[:, :, 0]

    # limiars
    th1 = 128
    th2 = 254

    ib = (h_channel > th1) & (h_channel < th2)
    ib = ib.astype(np.uint8) * 255
    ib = cv2.bitwise_not(ib)

    se_opening = cv2.getStructuringElement(cv2.MORPH_ELLIPSE, (15, 15))

    return cv2.morphologyEx(ib, cv2.MORPH_OPEN, se_opening)
