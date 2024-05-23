from datetime import datetime
from enum import Enum


class Direction(Enum):
    BACKWARD = "HEAD 180",
    STAY = "STOP",
    LEFT = "HEAD 90",
    RIGHT = "HEAD -90",
    FORWARD = "MOVE 2000"


class SonarInfo:
    def __init__(self, id_sonar, distance, timestamp):
        self.id = id_sonar
        self.distance = distance
        self.timestamp = timestamp

    def display_info(self):
        print(f"Id: {self.id}\nDistance: {self.distance} cm, Last update: {self.timestamp}")


def create_sonar(new_arr_sonar):
    for i in range(8):
        new_arr_sonar.append(SonarInfo(i, -1, datetime.now()))


def update_sonar_info(sonars, arr_sonar):
    for sonarInfo in sonars:
        for sonar in arr_sonar:
            if sonar.id == sonarInfo['sonar_number']:
                sonar.distance = (sonarInfo['sonar_range'] / 10)
                sonar.timestamp = datetime.now()


def print_sonar_info(sonars):
    for sonarInfo in sonars:
        sonarInfo.display_info()


def detect_obj(arr_sonar):
    for sonar in arr_sonar:
        if 0 < sonar.distance < 70 and sonar.id in (3, 4):
            print("STOP GIGANTE DO 3 E 4")
            return True
        if 0 < sonar.distance < 60 and sonar.id in (2, 5):
            print("STOP GIGANTE DO 2 E 5")
            return True
    return False


def detects_an_object_right(sonars):
    return 0 < sonars[7].distance <= 70


def detects_an_object_right(sonars):
    if not (0 < sonars[0].distance <= 50 or 0 < sonars[1].distance <= 50):
        print("TENHO CENAS À MINHA DIREITA")
        return True
    else:
        return False


def detects_an_object_ahead(sonars):
    for sonar in sonars:
        if (sonar.id in (2, 3) and 0 < sonar.distance < 50) or (0 < sonar.id in (4, 5) and sonar.distance < 40):
            for s in sonars:
                if 0 < s.distance > 50 and s.id in (0, 1):
                    return Direction.LEFT
                elif 0 < s.distance > 50 and s.id in (6, 7):
                    return Direction.RIGHT
    return Direction.STAY


"""
if __name__ == '__main__':
    sonars = []
    create_sonar(sonars)
    new_sonar = [
        {'sonar_number': 1, 'sonar_range': 300},
        {'sonar_number': 3, 'sonar_range': 400},
        {'sonar_number': 7, 'sonar_range': 800}
    ]
    update_sonar_info(new_sonar, sonars)
    direction = detects_an_object_ahead(sonars)
    boolean = detects_an_object_left(sonars)
    print(direction)
"""
