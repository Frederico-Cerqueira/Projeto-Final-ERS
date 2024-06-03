class SipInfo:
    def __init__(self, sonars, coordinates, motors):
        self.sonars = sonars
        self.coordinates = coordinates
        self.motors = motors

    def get_sonars(self):
        return self.sonars

    def get_coordinates(self):
        return self.coordinates

    def get_motors(self):
        return self.motors
