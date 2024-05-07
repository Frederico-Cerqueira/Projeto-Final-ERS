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

    This function reads the input RGB image, resizes it to 25% of the original size,
    performs object detection on the resized image in the HSV color space,
    and displays the original RGB image, the converted HSV image,
    as well as the thresholded and processed images resulting from object detection.
    """
    # Read the input RGB image
    image = cv2.imread(img)

    # Resize the image to 25% of the original size
    image = cv2.resize(image, None, fx=0.25, fy=0.25)

    # Convert the image from RGB to HSV
    image_hsv = cv2.cvtColor(image, cv2.COLOR_BGR2HSV)

    Ib, Ib2,Ib3, x, y, w, h = detect_object(image_hsv[:,:,0])

    color = (0, 255, 0)
    #cv2.imshow('Original', image)
    cv2.imshow('HSV', image_hsv)
    cv2.imshow('Thresholded', Ib * 255)
    cv2.imshow('Processed', Ib2 * 255)
    #cv2.imshow('.', Ib3)
    # Desenhe o retângulo sobre a imagem Ib3
    cv2.rectangle(image, (x, y), (x + w, y + h), color, 5)

    #cv2.circle(Ib3, (x, y), 15, (100,100,100), -1)
    #cv2.imshow('Point', Ib3)



    # Exiba ou salve a imagem com o retângulo desenhado
    cv2.imshow('Imagem com retângulo', image)



    print("x: ", x)
    print("y: ", y)
    cv2.waitKey(0)
    cv2.destroyAllWindows()

    return image_hsv


def detect_object(hsv_image):
    """
        Detects objects in an image using thresholding and morphological operations.

        Args:
            hsv_image (numpy.ndarray): Image in the HSV color space.

        This function applies thresholding to the input HSV image, performs morphological
        opening and closing operations to remove noise and fill gaps in the detected object,
        finds contours of the detected object, and returns a binary matrix representing the
        thresholded image, the processed binary image, and the coordinates of the bounding
        box around the detected object.

        Returns:
            tuple: A tuple containing:
                - matrix (numpy.ndarray): Binary matrix after thresholding.
                - Ib2 (numpy.ndarray): Processed binary image after morphological operations.
                - x (int): X-coordinate of the top-left corner of the bounding box.
                - y (int): Y-coordinate of the top-left corner of the bounding box.
        """
    threshold = 50
    matrix = (hsv_image > threshold).astype(np.uint8)

    se = cv2.getStructuringElement(cv2.MORPH_ELLIPSE, (4, 4))
    Ib2 = cv2.morphologyEx(matrix, cv2.MORPH_OPEN, se)
    Ib2 = cv2.morphologyEx(Ib2, cv2.MORPH_CLOSE, se)

    contours, _ = cv2.findContours(Ib2, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)
    # Filtragem de contornos por área
    filtered_contours = []
    for contour in contours:
        area = cv2.contourArea(contour)
        # Defina uma faixa de área adequada para os contornos que deseja manter
        if 100 < area < 5000:  # Ajuste esses valores conforme necessário
            filtered_contours.append(contour)

    # Desenha os contornos filtrados em uma nova matriz binária
    Ib3 = np.zeros_like(Ib2)
    cv2.drawContours(Ib3, filtered_contours, -1, 255, thickness=cv2.FILLED)

    # Encontra o retângulo delimitador dos contornos filtrados
    x, y, w, h = cv2.boundingRect(Ib3)

    return matrix, Ib2, Ib3, x, y, w, h


# THE COORDINATES X AND Y ARE NOT BEING WELL SENT

if __name__ == '__main__':
    """
    coke_can = Image.open(in_file('coke_can.jpg'))
    bw_coke_can = grayscale(coke_can)
    bw_coke_can.save(out_file('coke_can.jpg'))
    """
    convert_rgb_to_hsv(in_file('yogurt.jpg'))
