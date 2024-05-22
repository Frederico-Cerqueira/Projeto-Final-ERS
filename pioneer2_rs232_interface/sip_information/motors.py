from datetime import datetime


class MotorsInfo:
    def __init__(self, on, timestamp):
        self.on = on
        self.timestamp = timestamp

    def display_info(self):
        print(f"Motors: {self.on} , Last update: {self.timestamp}")


def update_motors_info(sip, motors):
    motors.on = sip['motor_status']
    motors.timestamp = datetime.now()
