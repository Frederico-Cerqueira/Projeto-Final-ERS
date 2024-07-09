from datetime import datetime


class MotorsInfo:
    """Class to represent the motors information."""
    def __init__(self, on, timestamp):
        self.on = on
        self.timestamp = timestamp

    def display_info(self):
        """Display the motorsInfo information."""
        print(f"Motors: {self.on} , Last update: {self.timestamp}")


def update_motors_info(sip, motors):
    """Update the motorsInfo information."""
    motors.on = sip['motor_status']
    motors.timestamp = datetime.now()
