from datetime import datetime
from enum import Enum


class Direction(Enum):
    BACKWARD = -1,
    STAY = 0
    LEFT = 1,
    RIGHT = 2,
    FORWARD = 3


class Rotate:
    def __init__(self, direction, sonar_id):
        self.direction = direction
        self.sonar_id = sonar_id


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
        if sonar.distance < 50 and sonar.id in (2, 3, 4, 5):
            return "STOP"




if __name__ == "__main__":
    sonars_data = [
        {'sonar_number': 0, 'sonar_range': 100},
        {'sonar_number': 1, 'sonar_range': 200},

        {'sonar_number': 2, 'sonar_range': 300},
        {'sonar_number': 3, 'sonar_range': 20},
        {'sonar_number': 4, 'sonar_range': 500},
        {'sonar_number': 5, 'sonar_range': 600},

        {'sonar_number': 6, 'sonar_range': 700},
        {'sonar_number': 7, 'sonar_range': 800}
    ]

    arr_sonar = []
    create_sonar(arr_sonar)
    update_sonar_info(sonars_data, arr_sonar)
    direction = detect_obj(arr_sonar)
    print(direction)
