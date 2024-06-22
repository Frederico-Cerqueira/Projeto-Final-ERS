from picamera2 import Picamera2
from io import BytesIO
from PIL import Image
import numpy as np


def take_photo():
    """
        Captures a photo using the PiCamera2, processes it, and returns it as a NumPy array.

        @return: A NumPy array representing the captured image.
    """
    picam2 = Picamera2()
    config = picam2.create_still_configuration()
    picam2.configure(config)
    picam2.start()

    stream = BytesIO()
    picam2.capture_file(stream, format='jpeg')
    stream.seek(0)

    image = Image.open(stream)
    image_np = np.array(image)

    picam2.stop()

    return image_np
