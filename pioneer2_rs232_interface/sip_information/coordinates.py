from datetime import datetime


class CoordinatesInfo:
    def __init__(self, x, y, degree, timestamp):
        self.x = x
        self.y = y
        self.degree = degree
        self.timestamp = timestamp

    def display_info(self):
        print(f"Cordenate (x,y): {self.x},{self.y}\nDegree: {self.degree}º, Last update: {self.timestamp}")


def update_coordinate_info(sip, coordinates):
    for info in sip:
        coordinates.x = info['x_pos']
        coordinates.y = info['y_pos']
        coordinates.degree = info['th_pos']
        coordinates.timestamp = datetime.now()

