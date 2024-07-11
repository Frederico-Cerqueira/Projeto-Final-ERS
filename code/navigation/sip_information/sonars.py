from datetime import datetime
from enum import Enum


class Direction(Enum):
    """Enum class to represent the direction of the robot."""
    BACKWARD = "HEAD 180",
    STAY = "STOP",
    LEFT = "HEAD 90",
    RIGHT = "HEAD -90",
    FORWARD = "MOVE 2000"


class SonarInfo:
    """Class to represent the sonar information."""

    def __init__(self, id_sonar, distance, timestamp):
        self.id = id_sonar
        self.distance = distance
        self.timestamp = timestamp

    def display_info(self):
        """Display the sonar information."""
        print(f"Id: {self.id}\nDistance: {self.distance} cm, Last update: {self.timestamp}")


def create_sonar(new_arr_sonar):
    """Create an array with the initial sonar information."""
    for i in range(8):
        new_arr_sonar.append(SonarInfo(i, -1, datetime.now()))


def update_sonar_info(sonars, arr_sonar):
    """Update the sonar information."""
    for sonarInfo in sonars:
        for sonar in arr_sonar:
            if sonar.id == sonarInfo['sonar_number']:
                sonar.distance = (sonarInfo['sonar_range'] / 10)
                sonar.timestamp = datetime.now()


def print_sonar_info(sonars):
    """Print the array with all the sonar information."""
    for sonarInfo in sonars:
        sonarInfo.display_info()


def detect_obj(arr_sonar):
    """Detect an object in front of the robot."""
    for sonar in arr_sonar:
        if 0 < sonar.distance < 60 and sonar.id in (3, 4):
            #print("STOP GIGANTE DO 3 E DO 4")
            return True
    return False


def detects_an_object_right(sonars):
    """Detect an object on the right side of the robot."""
    return 0 < sonars[7].distance <= 50


def detects_an_object_left(sonars):
    """Detect an object on the left side of the robot."""
    return 0 < sonars[0].distance <= 50


def detects_an_object_ahead(sonars):
    """Detect an object ahead of the robot and return the direction to turn."""
    for sonar in sonars:
        if (sonar.id in (3, 4) and 0 < sonar.distance < 60) or (0 < sonar.id in (2, 5) and 0 < sonar.distance < 60):
            for s in sonars:
                if (not 0 < s.distance < 60) and s.id == 0:
                    return Direction.LEFT
                elif (not 0 < s.distance < 60) and s.id == 7:
                    return Direction.RIGHT
    return Direction.STAY
