from PIL import Image
from utils import in_file, out_file

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


if __name__ == '__main__':
    coke_can = Image.open(in_file('coke_can.jpg'))
    bw_coke_can = grayscale(coke_can)
    bw_coke_can.save(out_file('coke_can.jpg'))
