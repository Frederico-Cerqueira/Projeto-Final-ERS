from picamera2 import Picamera2
from io import BytesIO
from PIL import Image
import numpy as np


def init_cam():
    print("Initializing camera.")
    picam2 = Picamera2()
    config = picam2.create_still_configuration()
    picam2.configure(config)
    picam2.start()
    return picam2
    
    
def take_photo(picam2):
    """
        Captures a photo using the PiCamera2, processes it, and returns it as a NumPy array.

        @return: A NumPy array representing the captured image.
    """
    stream = BytesIO()
    picam2.capture_file(stream, format='jpeg')
    stream.seek(0)

    image = Image.open(stream)
    image_np = np.array(image)

    return image_np
