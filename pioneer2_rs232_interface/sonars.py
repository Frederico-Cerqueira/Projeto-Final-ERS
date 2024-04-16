import time
from datetime import datetime


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
        if(sonarInfo.id == 4):
            sonarInfo.display_info()
