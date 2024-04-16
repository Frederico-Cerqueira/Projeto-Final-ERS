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
        new_arr_sonar.append(SonarInfo(i, 0, datetime.now()))


def update_sonar_info(sonars, arr_sonar):
    for sonarInfo in sonars:
        for sonar in arr_sonar:
            if sonar.id == sonarInfo['sonar_number']:
                sonar.distance = (sonarInfo['sonar_range'] / 10)
                sonar.timestamp = datetime.now()


def print_sonar_info(sonars):
    for sonarInfo in sonars:
        sonarInfo.display_info()


# Exemplo de uso:
if __name__ == "__main__":
    sonars_data = [
        {'sonar_number': 0, 'sonar_range': 1000},
        {'sonar_number': 1, 'sonar_range': 2000},
        {'sonar_number': 2, 'sonar_range': 3000},
        {'sonar_number': 3, 'sonar_range': 4000},
        {'sonar_number': 4, 'sonar_range': 5000},

    ]

    arr_sonar = []
    create_sonar(arr_sonar)
    update_sonar_info(sonars_data, arr_sonar)
    print_sonar_info(arr_sonar)
    sonars_data.append({'sonar_number': 5, 'sonar_range': 6000})
    sonars_data.append({'sonar_number': 6, 'sonar_range': 7000})
    time.sleep(60)
    update_sonar_info(sonars_data, arr_sonar)
    print("")

    print("")
    print_sonar_info(arr_sonar)
