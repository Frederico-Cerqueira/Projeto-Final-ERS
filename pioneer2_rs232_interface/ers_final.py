import sys
from datetime import datetime

sys.path.append("./serial_communication")
sys.path.append("./serial_communication/communication_protocol")

from serial_communication.serial_communication import SerialCommunication
from sonars import create_sonar

DISTANCE_ERROR_RANGE = range(-30, 30)
ANGLE_ERROR_RANGE = range(-3, 3)


class ERS:
    def __init__(self, port, baudrate):
        self.sip_info = None
        self.command = None
        self.last_command = None
        self.initial_coordinates = {
            'x_pos': None,
            'y_pos': None,
            'th_pos': None
        }
        self.expected_final_coordinates = {
            'x_pos': None,
            'y_pos': None,
            'th_pos': None
        }
        self.init_time_sip = datetime.now().timestamp()
        self.init_time_pulse = datetime.now().timestamp()

        # Start serial communication
        self.__serial_communication = SerialCommunication(port, baudrate)

        # Establish communication with the robot if the serial connection is started.
        if self.__serial_communication.is_connected():
            self.__establish_communication()

    def __establish_communication(self):
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
        if self.__serial_communication.is_connected():
            # Disables the motors
            self.send_command('STOP', None)

            # Close server and client connection
            self.send_command('CLOSE')

            # Terminate serial connection
            self.__serial_communication.disconnect()

    def send_command(self, command, arg=None):
        self.__serial_communication.send_command(command, arg)

    def send_pulse(self):
        # Keep the robot awake
        print("Sending pulse")
        self.send_command('PULSE')

    def check_pulse(self):
        final = datetime.now().timestamp()
        init = self.init_time_pulse
        if self.__serial_communication.is_connected() and (final - init > 1.500):
            # Send Pulse
            self.init_time_pulse = datetime.now().timestamp()
            self.send_pulse()

    #TODO
    def check_photo(self):
        #Tem de ver se já passou x tempo para tirar proxima foto
        pass

    def run(self, machine):

        # Initial time sip and pulse
        self.init_time_pulse, self.init_time_sip = datetime.now().timestamp()

        sonars = []
        create_sonar(sonars)

        while True:
            self.check_pulse()
            # tira foto e processa? assim vai ter prioridade sob obj
            # só tira foto aqui e no process_sip vê se há lixo? assim posso passar por cima do lixo
            self.check_photo()
            machine.state_machine(ers=self, sonars=sonars)
