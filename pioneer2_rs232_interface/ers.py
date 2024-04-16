# -*- coding: utf-8 -*-

# Define import path
import sys
import queue
from command import Command
from sonars import update_sonar_info, print_sonar_info
from serial_communication.serial_communication import SerialCommunication
from tests.timer import *

sys.path.append("./serial_communication")
sys.path.append("./serial_communication/communication_protocol")


class ERS:
    """ Class that allows communication with the Pioneer2 robot through a serial connection. """

    def __init__(self, port, baudrate):

        self.__running_flag = None
        self.sip_info = None
        self.__command = None
        self.true = False

        # Start serial communication
        self.__start_serial_connection(port, baudrate)

        # Create commands queue
        self.__commands_queue = queue.Queue()

        self.__execute_commands_queue = queue.Queue()

        # Establish communication with the robot if the serial connection is started.
        if self.__serial_communication.is_connected():
            self.__establish_communication()

    def __start_serial_connection(self, port, baudrate):
        self.__serial_communication = SerialCommunication(port, baudrate)

    def __establish_communication(self):
        """ Synchronize communication with the robot and start the robot's servers and motors. """
        print("Pioneer2 RS-232 Interface - Establishing Connection")

        # Send synchronization packets and obtain the responses for each one.
        self.__send_command('SYNC0')
        self.__send_command('SYNC1')
        self.__send_command('SYNC2')

        # Starts the controller
        self.__send_command('OPEN')

        # Request configuration SIP
        self.__send_command('CONFIG')

        # Enables the motors
        self.__send_command('ENABLE', 1)

        # RResets server to 0,0,0 origin
        self.__send_command('SETO')

    def add_console_command(self, command):
        """Add a command to the command queue."""
        self.__commands_queue.put(command)

    def __process_sip(self):
        sip_info_aux = self.__serial_communication.get_sip()

        if sip_info_aux is not None:
            self.sip_info = sip_info_aux
        # else:
        # print("unavailable SIP ")
        # self.sip_info = None

    def __process_command(self, command):
        print('Command received:' + command.comando + ' ' + str(command.args))

        # Print last SIP
        if command.comando == 'S':
            print(self.sip_info)

        # If the command is to turn off the interface
        elif command.comando == 'EXIT':
            self.turn_off()
            self.__running_flag = False

        # Otherwise, if the serial communication is active, attempt to send the command to the robot
        elif self.__serial_communication.is_connected():
            self.__send_command(command.comando, command.args)

    def __process_successive_commands(self):
        if self.sip_info is None and self.__command is None:  # 1st command
            self.__command = self.__commands_queue.get()
            print("execute the first command")
            self.__process_command(self.__command)
        else:
            if self.sip_info is not None:
                if self.sip_info['motor_status']:  # 1st phase of completion
                    self.__command = None
                if not self.sip_info['motor_status'] and self.__command is None:  # 2nd phase of completion
                    self.__command = self.__commands_queue.get()
                    # if self.__command is not None:
                    print("execute the second command")
                    self.__process_command(self.__command)

    def __send_command(self, command, arg=None):
        self.__serial_communication.send_command(command, arg)

    def turn_off(self):
        if self.__serial_communication.is_connected():
            # Disables the motors
            self.__send_command('ENABLE', 0)

            # Close server and client connection
            self.__send_command('CLOSE')

            # Terminate serial connection
            self.__serial_communication.disconnect()

    def run(self):
        print("Pioneer2 RS-232 Interface - Running")
        self.__running_flag = True

        # Initial time
        tempo_init = final_time = datetime.now().timestamp()

        # Initial and final pulse time
        initial_pulse_time, final_pulse_time = datetime.now().timestamp()
        sonars = []
        while self.__commands_queue.qsize() > 0 or self.__command is not None:
            # print("size:", self.__commands_queue.qsize())

            if self.__serial_communication.check_sip_availibility() and (final_time - tempo_init > 0.100):
                tempo_init = datetime.now().timestamp()
                self.__process_sip()

            if self.sip_info is not None:
                sonar_info = self.sip_info['sonars']
                updated_sonars = update_sonar_info(sonar_info, sonars)
                print_sonar_info(updated_sonars)

            self.__process_successive_commands()

            # Keep the robot awake
            if self.__serial_communication.is_connected() and (final_pulse_time - initial_pulse_time > 1.500):
                initial_pulse_time = datetime.now().timestamp()
                print("Pulse")
                self.__serial_communication.send_command('PULSE')

            # Calculate times to create a set_interval
            final_time = datetime.now().timestamp()
            final_pulse_time = datetime.now().timestamp()

        print("Pioneer2 RS-232 Interface - Shutdown")


def current_position(pioneer):
    print("x pos:", pioneer.sip_info['x_pos'])
    print("y pos:", pioneer.sip_info['y_pos'])
    print("Heading pos:", pioneer.sip_info['th_pos'])


if __name__ == '__main__':
    pioneer2 = ERS('COM10', 9600)
    try:
        # pioneer2.turn_off()
        # pioneer2.add_console_command(Command('MOVE', 6000))
        pioneer2.add_console_command(Command('MOVE', 1000))
        pioneer2.add_console_command(Command('SONAR', 1))
        # pioneer2.add_console_command(Command('POLLING', "\001\002\003\004\005\006\007\010"))
        pioneer2.add_console_command(Command('MOVE', 20000))
        pioneer2.add_console_command(Command('MOVE', 100))

        # pioneer2.add_console_command(Command('HEAD', 90))
        # pioneer2.add_console_command(Command('SETO', None))
        # pioneer2.add_console_command(Command('SONAR', 0))
        # pioneer2.add_console_command(Command('BUMP_STALL', 0))
        # pioneer2.add_console_command(Command('EXIT', None))
        pioneer2.run()
        pioneer2.turn_off()
        current_position(pioneer2)

    except Exception as e:
        print("Error during execution:", e)
        pioneer2.turn_off()
