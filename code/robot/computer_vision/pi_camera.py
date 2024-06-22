from take_photo import take_photo
from image_processing import image_processing
from detect_trash_in_area import detect_trash_in_area

# Global variable to keep track of whether trash is detected or not.
# Initially set to None, indicating no detection has been performed yet.
trash_detected = None


def trash_lookup():
    """
        Captures an image, processes it, and updates the global variable
        trash_detected based on whether trash is detected in the processed image.
    """
    global trash_detected
    img = take_photo()
    processed_image = image_processing(img)
    trash_detected = detect_trash_in_area(processed_image)


def trash_collected():
    """
        Sets the global variable trash_detected to false,
        representing the moment when the robot collects the trash
    """
    global trash_detected
    trash_detected = False


def get_trash_detected():
    """
        Retrieves the current value of the global variable trash_detected.

        @return: The current value of the global variable trash_detected
    """
    global trash_detected
    return trash_detected
