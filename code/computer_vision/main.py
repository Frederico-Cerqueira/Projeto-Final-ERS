from utils import in_file
import cv2
import numpy as np


def convert_rgb_to_hsv(img):
    # Read the input RGB image
    image = cv2.imread(img)

    # Resize the image to 25% of the original size
    image = cv2.resize(image, None, fx=0.25, fy=0.25)

    # Convert the image from RGB to HSV
    image_hsv = cv2.cvtColor(image, cv2.COLOR_BGR2HSV)

    matrix, opening, dilation = detect_object(image_hsv[:, :, 0])

    cv2.imshow('img', image)
    cv2.imshow('HSV', image_hsv)

    # não sei bem porque é que tenho de fazer *255
    cv2.imshow('matrix', matrix * 255)
    cv2.imshow('opening', opening * 255)
    cv2.imshow('dilation', dilation * 255)
    cv2.rectangle(opening, (70, 150), (570, 450), (0, 0, 255), 2)  # Blue rectangle with thickness 2
    cv2.imshow('area', opening * 255)

    cv2.waitKey(0)
    cv2.destroyAllWindows()

    return image_hsv


def detect_object(hsv_image):
    threshold = 64
    # Pixels with a value greater than the threshold become 1 (white), and those below become 0 (black) in the resulting
    # binary matrix (matrix).
    matrix = (hsv_image > threshold).astype(np.uint8)
    kernel = np.ones((5, 5), np.uint8)
    opening_kernel = np.ones((12, 12), np.uint8)
    # Morphological Operations:
    #closing = cv2.morphologyEx(matrix, cv2.MORPH_CLOSE, kernel)
    opening = cv2.morphologyEx(matrix, cv2.MORPH_OPEN, opening_kernel)
    dilation = cv2.dilate(opening, kernel, iterations=1)

    return matrix, opening, dilation


def set_area_to_be_checked():
    # Carregar a imagem
    image = cv2.imread(in_file('chocolate_milk.jpg'))  # Substitua por seu nome de arquivo real
    # Resize the image to 25% of the original size
    image = cv2.resize(image, None, fx=0.25, fy=0.25)

    pt1 = (70, 150)  # Ponto inicial (canto superior esquerdo)
    pt2 = (570, 450)  # Ponto final (canto inferior direito)
    color = (255, 0, 0)  # BGR (azul)
    thickness = 3
    cv2.rectangle(image, pt1, pt2, color, thickness)
    cv2.imshow('Imagem com Retângulo', image)
    cv2.waitKey(0)  # Aguardar pressionar uma tecla
    cv2.destroyAllWindows()


if __name__ == '__main__':
    convert_rgb_to_hsv(in_file('milk_pack.jpg'))
    # set_area_to_be_checked()
