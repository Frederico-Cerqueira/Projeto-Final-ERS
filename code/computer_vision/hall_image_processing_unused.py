import cv2
import numpy as np


def convert_rgb_to_hsv(img_path):

    image = cv2.imread(img_path)
    image = cv2.resize(image, None, fx=0.15, fy=0.15)
    image_hsv = cv2.cvtColor(image, cv2.COLOR_BGR2HSV)

    matrix, closing, opening, dilation = highlight_object(image_hsv[:, :, 0])
    dilation = dilation * 255

    cv2.imshow('img', image)
    cv2.imshow('HSV', image_hsv)
    cv2.imshow('matrix', matrix * 255)
    cv2.imshow('closing', closing * 255)
    cv2.imshow('opening', opening * 255)
    cv2.imshow('dilation', dilation)

    cv2.waitKey(0)
    cv2.destroyAllWindows()


def highlight_object(hsv_image):
    threshold = 64
    matrix = (hsv_image > threshold).astype(np.uint8)
    kernel = np.ones((5, 5), np.uint8)
    opening_kernel = np.ones((12, 12), np.uint8)
    closing_kernel = np.ones((4, 4), np.uint8)
    # Morphological Operations:
    closing = cv2.morphologyEx(matrix, cv2.MORPH_CLOSE, closing_kernel)
    opening = cv2.morphologyEx(closing, cv2.MORPH_OPEN, opening_kernel)
    dilation = cv2.dilate(opening, kernel, iterations=2)

    return matrix, closing, opening, dilation
