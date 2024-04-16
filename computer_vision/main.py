from PIL import Image
from utils import in_file, out_file
import cv2
import numpy as np

# To convert any color to its approximate gray level, you must first obtain its red, green, and blue primitives (from
# the RGB scale). Then add 30% of the red plus 59% of the green plus 11% of the blue, regardless of the scale used.
RED_PRIMITIVE = 0.30
GREEN_PRIMITIVE = 0.59
BLUE_PRIMITIVE = 0.11


def grayscale(colored):
    """
    Converts a colored image to grayscale.

    Args:
        colored (PIL.Image.Image): a colored image.

    Returns:
        PIL.Image.Image: a grayscale version of the input image.

    This function takes a colored image as input and converts it to grayscale. It iterates through each pixel in the
    input image, calculates the luminance using a weighted sum of the RGB components, and assigns the same luminance
    value to each RGB component of the corresponding pixel in the output image. The resulting is returned.
      """
    w, h = colored.size
    img = Image.new('RGB', (w, h))
    for x in range(w):
        for y in range(h):
            pxl = colored.getpixel((x, y))
            lum = int(pxl[0] * RED_PRIMITIVE + pxl[1] * GREEN_PRIMITIVE + pxl[2] * BLUE_PRIMITIVE)
            img.putpixel((x, y), (lum, lum, lum))
    return img


def convert_rgb_to_hsv(img):
    """
    Converts an RGB image to the HSV color space.

    Args:
        img (str): Path to the input RGB image file.

    This function reads the input RGB image, resizes it to 25% of the original size, converts it to the HSV color space,
    and displays both the original RGB image and the converted HSV image in separate windows.
    """
    # Read the input RGB image
    image = cv2.imread(img)

    # Resize the image to 25% of the original size
    image = cv2.resize(image, None, fx=0.25, fy=0.25)

    # Convert the image from RGB to HSV
    image_hsv = cv2.cvtColor(image, cv2.COLOR_BGR2HSV)

    Ib, Ib2, x, y = detect_object(image_hsv[:,:,0])

    cv2.imshow('Original', image)
    cv2.imshow('HSV', image_hsv)
    cv2.imshow('Thresholded', Ib * 255)
    cv2.imshow('Processed', Ib2 * 255)
    cv2.waitKey(0)
    cv2.destroyAllWindows()

    return image_hsv


def detect_object(I_H):
    Th = 40
    Ib = (I_H > Th).astype(np.uint8)

    se = cv2.getStructuringElement(cv2.MORPH_ELLIPSE, (4, 4))
    Ib2 = cv2.morphologyEx(Ib, cv2.MORPH_OPEN, se)
    Ib2 = cv2.morphologyEx(Ib2, cv2.MORPH_CLOSE, se)

    contours, _ = cv2.findContours(Ib2, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)
    if contours:  # Verifica se a lista de contornos não está vazia
        contour = contours[0]  # Pega o primeiro contorno
        x, y, _, _ = cv2.boundingRect(contour)
    else:
        x = -1
        y = -1

    return Ib, Ib2, x, y


"""
def detect_objects_hue_threshold(hsv_image, hue_threshold):
    # Separando o canal de matiz (H)
    hue_channel = hsv_image[:, :, 0]

    # Aplicando um limiar para identificar objetos
    objects_mask = hue_channel > hue_threshold

    return objects_mask


# Definindo um limiar para a componente H (matiz)
hue_threshold = 100

# Detectando objetos com base no limiar da componente H
objects_mask = detect_objects_hue_threshold(convert_rgb_to_hsv(in_file('coke_can.jpg')), hue_threshold)

# Convertendo a máscara para tipo uint8 para exibição
objects_mask = objects_mask.astype(np.uint8) * 255

# Exibindo a máscara resultante
cv2.imshow('Objects Mask', objects_mask)
cv2.waitKey(0)
cv2.destroyAllWindows()
"""


if __name__ == '__main__':
    """
    coke_can = Image.open(in_file('coke_can.jpg'))
    bw_coke_can = grayscale(coke_can)
    bw_coke_can.save(out_file('coke_can.jpg'))
    """
    convert_rgb_to_hsv(in_file('yogurt.jpg'))
