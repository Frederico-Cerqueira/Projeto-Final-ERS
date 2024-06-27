import sys

from datetime import datetime

sys.path.append("./serial_communication")
sys.path.append("./serial_communication/communication_protocol")

from sip_information.coordinates import CoordinatesInfo
from sip_information.motors import MotorsInfo
from sip_information.sip_info import SipInfo
from serial_communication.serial_communication import SerialCommunication
from sip_information.sonars import create_sonar
from ..computer_vision.pi_camera import trash_lookup

DISTANCE_ERROR_RANGE = range(-30, 30)
ANGLE_ERROR_RANGE = range(-3, 3)
IMAGE_PROCESSING_TIME = 15

class ERS:
    def __init__(self, port, baudrate):
        self.sip_info = []
        self.command = None
        self.init_time_sip = datetime.now().timestamp()
        self.init_time_pulse = datetime.now().timestamp()
        self.init_time_image = None

        # Start serial communication
        self.serial_communication = SerialCommunication(port, baudrate)

        # Establish communication with the robot if the serial connection is started.
        if self.serial_communication.is_connected():
            self.establish_communication()

    def establish_communication(self):
        """ Synchronize communication with the robot and start the robot's servers and motors. """
        print("Pioneer2 RS-232 Interface - Establishing Connection")

        # Send synchronization packets and obtain the responses for each one.
        self.send_command('SYNC0')
        self.send_command('SYNC1')
        self.send_command('SYNC2')
        # Starts the controller
        self.send_command('OPEN')
        # Request configuration SIP
        self.send_command('CONFIG')
        # Enables the motors
        self.send_command('ENABLE', 1)
        # Resets server to 0,0,0 origin
        self.send_command('SETO')

    def turn_off(self):
        if self.serial_communication.is_connected():
            self.send_command('STOP', None)
            self.send_command('CLOSE')
            self.serial_communication.disconnect()

    def send_command(self, command, arg=None):
        self.serial_communication.send_command(command, arg)

    def check_pulse(self):
        final = datetime.now().timestamp()
        init = self.init_time_pulse
        if self.serial_communication.is_connected() and (final - init > 1.500):
            # Send Pulse
            self.init_time_pulse = datetime.now().timestamp()
            self.send_command('PULSE')

    def take_photo(self):
        final = datetime.now().timestamp()
        init = self.init_time_image
        if init is None or final - init > IMAGE_PROCESSING_TIME:
            trash_lookup()


    # E2
    def get_sip(self):
        initial = self.init_time_sip
        current = datetime.now().timestamp()
        if self.serial_communication.check_sip_availability() and (current - initial > 0.100):
            self.init_time_sip = datetime.now().timestamp()
            sip_info_aux = self.serial_communication.get_sip()
            if sip_info_aux is not None:
                if len(self.sip_info) == 0:
                    self.sip_info.append(sip_info_aux)
                    pass
                elif sip_info_aux != self.sip_info[-1]:
                    self.sip_info.append(sip_info_aux)
                    pass

    def run(self, machine):
        # Initial time sip and pulse
        self.init_time_pulse = datetime.now().timestamp()
        self.init_time_sip = datetime.now().timestamp()

        sonars = []
        create_sonar(sonars)
        coordinates = CoordinatesInfo(0, 0, 0, datetime.now())
        motors = MotorsInfo(False, datetime.now())
        sip = SipInfo(sonars, coordinates, motors)

        while True:
            self.get_sip()
            self.check_pulse()
            self.take_photo()
            machine.state_machine(ers=self, sip=sip)