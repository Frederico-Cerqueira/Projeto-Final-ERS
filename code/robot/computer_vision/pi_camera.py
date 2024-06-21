from take_photo import take_photo
from image_processing import image_processing
from detect_trash_in_area import detect_trash_in_area

trash_detected = None


def trash_lookup():
    global trash_detected
    img = take_photo()
    processed_image = image_processing(img)
    trash_detected = detect_trash_in_area(processed_image)


def trash_collected():
    global trash_detected
    trash_detected = False


def get_trash_detected():
    global trash_detected
    return trash_detected