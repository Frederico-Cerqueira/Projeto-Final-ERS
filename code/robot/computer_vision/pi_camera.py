from picamera2 import Picamera2
import cv2
import time


def take_photo():
    picam2 = Picamera2()
    picam2.start()
    time.sleep(1)
    image = picam2.capture_image("main")
    cv2.imshow('pi_image', image)



