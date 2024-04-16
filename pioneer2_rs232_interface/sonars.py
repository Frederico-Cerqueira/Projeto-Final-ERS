from tests.timer import *


class SonarInfo:
    def __init__(self, id_sonar, distance, timestamp):
        self.distance = distance
        self.id = id_sonar
        self.timestamp = timestamp

    def display_info(self):
        print("Id: " + self.id + "\nDistance: " + str(self.distance) + " ,last update: " + str(self.timestamp))


def update_sonar_info(sonars, arr_sonar):
    if len(arr_sonar) == 0:
        for sonarInfo in sonars:
            for i in range(7):
                if i == sonarInfo['sonar_number']:
                    arr_sonar.append(SonarInfo(1, sonarInfo['sonar_range'] / 10, datetime.now().timestamp()))
        return arr_sonar
    else:
        for sonarInfo in sonars:
            for sonar in arr_sonar:
                if sonar.id == sonarInfo['sonar_number']:
                    sonar.distance = (sonarInfo['sonar_range'] / 10)
                    sonar.timestamp = datetime.now().timestamp()
        return arr_sonar


def print_sonar_info(sonars):
    for sonarInfo in sonars:
        sonarInfo.display_info()
