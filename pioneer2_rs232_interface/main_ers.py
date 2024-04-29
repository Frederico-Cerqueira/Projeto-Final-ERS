import queue
import time
import sys
from datetime import datetime

sys.path.append("./serial_communication")
sys.path.append("./serial_communication/communication_protocol")

from command import Command
from serial_communication.serial_communication import SerialCommunication
from sonars import create_sonar, detect_obj, update_sonar_info, detects_an_object_ahead, Direction

DISTANCE_ERROR_RANGE = range(-30, 30)
ANGLE_ERROR_RANGE = range(-3, 3)


class ERS:
    """ Class that allows communication with the Pioneer2 robot through a serial connection. """

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
        # Create commands queue
        self.__commands_queue = queue.Queue()
        # Start serial communication
        self.__serial_communication = SerialCommunication(port, baudrate)
        # self.initial_time = datetime.now().timestamp()

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

    def process_sip(self):
        sip_info_aux = self.__serial_communication.get_sip()
        print("SIP info aux: " + str(sip_info_aux))
        if sip_info_aux is not None:
            print("SIP info auxiliar is not NONE ")
            self.sip_info = sip_info_aux

    def send_command(self, command, arg=None):
        self.__serial_communication.send_command(command, arg)

    def state_machine(self):
        print("Pioneer2 RS-232 Interface - Running: cat version")

        # Initial time
        init_time = final_time = datetime.now().timestamp()

        # Initial and final pulse time
        initial_pulse_time = datetime.now().timestamp()
        final_pulse_time = datetime.now().timestamp()

        sonars = []
        create_sonar(sonars)

        # This while should end when all the commands are completed TODO
        while True:
            if self.__serial_communication.is_connected() and (final_pulse_time - initial_pulse_time > 1.500):
                # Send Pulse
                initial_pulse_time = datetime.now().timestamp()
                self.send_pulse()
            # Check SIP availability
            if self.__serial_communication.check_sip_availability() and (final_time - init_time > 0.100):
                print("AVAILABLE SIP")
                # Process SIP
                init_time = datetime.now().timestamp()
                self.process_sip()
                # There's a valid SIP?
                if self.sip_info is not None:
                    print("SIP IS NOT NONE")
                    # There's a valid SIP, check the sonars
                    # Has obstacle?
                    update_sonar_info(self.sip_info['sonars'], sonars)
                    if detect_obj(sonars):
                        print("Obstacle found!")
                        # Obstacle found!
                        # self.dodge_obstacle(sonars)
                        self.send_stop()
                        self.turn_off()
                    else:
                        print("NO OBSTACLE FOUND")
                        # There are no close obstacles, now we have to verify if there is any detectable trash
                        # Has trash?
                        if detect_trash():
                            print("Trash found!")
                            # Trash detected send a STOP command
                            self.send_stop()
                            """ Q: Do i have to make sure that stop was executed before turning off the robot?"""
                            self.turn_off()
                        else:
                            print("NO TRASH FOUND")
                            # No trash detected. Is the last command done?
                            # Motors off?
                            if self.sip_info['motor_status']:
                                # Motors on
                                # We do this to be able to distinguish if the motors are off because the command
                                # execution is already over or because it has not started yet
                                if self.command is not None:
                                    print("Saving current command")
                                    self.last_command = self.command
                                    self.command = None
                            else:
                                print("MOTORS ARE OFF")
                                # The motors are off, now we have to check if this is because the last command execution
                                # is done, or because it has not started yet
                                # Exec started?
                                if self.command_execution_started():
                                    print("MOTORS OFF BECAUSE THE COMMAND IS DONE")
                                    # This means that the motors are off because the command execution is already done
                                    # Now we have to test if the movement was completed with success
                                    # Movement completed?
                                    if self.movement_completed_with_success():
                                        print("Executing next command")
                                        # Send next command
                                        self.command = self.__commands_queue.get()
                                        self.process_command()
                                    else:
                                        # Resend last command
                                        print("Command not well completed, resending...")
                elif self.command is None:
                    # There is no valid sip which means that no command has been executed yet. Send 1st command
                    print("Executing the 1st command")
                    self.command = self.__commands_queue.get()
                    self.process_command()
            final_time = datetime.now().timestamp()
            final_pulse_time = datetime.now().timestamp()
            time.sleep(0.0019)

    def dodge_obstacle(self, sonars):
        self.command = Command('STOP', 0)
        self.process_command()
        direction = detects_an_object_ahead(sonars)
        print("Direction: ", direction)
        if direction == Direction.LEFT:
            self.command = Command('HEAD', 90)
            self.process_command()
            # self.dodge_obstacle_right(sonars)
        elif direction == Direction.RIGHT:
            self.command = Command('HEAD', -90)
            self.process_command()
            # self.dodge_obstacle_left(sonars)

    def command_execution_started(self):
        print("CHECKING IF THE EXECUTION HAS STARTED")
        print("COMMAND: ", self.command)
        if self.command is None:
            return True
        return False

    def process_command(self):
        print("EXECUTING COMMAND: ", self.command.name)
        if self.sip_info is not None:
            # Set initial coordinates
            self.initial_coordinates['x_pos'] = self.sip_info['x_pos']
            self.initial_coordinates['y_pos'] = self.sip_info['y_pos']
            self.initial_coordinates['th_pos'] = self.sip_info['th_pos']

        # Process command
        # If the command is to turn off the interface
        if self.command.name == 'EXIT':
            self.turn_off()

        # Otherwise, if the serial communication is active, attempt to send the command to the robot
        elif self.__serial_communication.is_connected():
            self.send_command(self.command.name, self.command.args)

        """ Q: Maybe we could simplify this function with the aux fun get_movement_direction """

    # TODO
    def movement_completed_with_success(self):
        print("Checking if the movement was completed with success")
        """
        # Access LAST command
        cmd_name = self.last_command.name
        args = self.last_command.args
        if cmd_name == 'MOVE':
            self.expected_final_coordinates['x_pos'] = self.initial_coordinates['x_pos'] + args
            self.expected_final_coordinates['y_pos'] = self.initial_coordinates['y_pos'] + args
            if self.expected_final_coordinates['x_pos'] - self.sip_info['x_pos'] not in DISTANCE_ERROR_RANGE:
                if self.expected_final_coordinates['y_pos'] - self.sip_info['y_pos'] not in DISTANCE_ERROR_RANGE:
                    return False
        elif cmd_name == 'HEAD':
            self.expected_final_coordinates['th_pos'] = self.initial_coordinates['th_pos'] + args
            if self.expected_final_coordinates['th_pos'] - self.sip_info['th_pos'] not in ANGLE_ERROR_RANGE:
                return False"""
        return True

    # TODO
    def resend_last_command(self):
        cmd_name = self.command.name
        mov_direction = self.get_movement_direction()
        args = self.expected_final_coordinates[mov_direction] - self.sip_info[mov_direction]
        self.command = Command(cmd_name, args)
        self.process_command()

    # TODO
    def get_movement_direction(self):
        """if self.sip_info['x_pos'] - self.initial_coordinates['x_pos'] not in DISTANCE_ERROR_RANGE:
            return 'x_pos'
        elif self.sip_info['y_pos'] - self.initial_coordinates['y_pos'] not in DISTANCE_ERROR_RANGE:
            return 'y_pos'
        elif self.sip_info['th_pos'] - self.initial_coordinates['th_pos'] not in DISTANCE_ERROR_RANGE:"""
        return 'th_pos'

    def send_pulse(self):
        # Keep the robot awake
        print("Sending pulse")
        self.send_command('PULSE')

    def send_stop(self):
        # Stop the robot
        self.send_command('STOP', 0)

    """ WARNING: This function is not being used"""
    def send_next_command(self):
        print("Executing next command")
        self.command = self.__commands_queue.get()
        self.process_command()

    def add_console_command(self, command):
        """Add a command to the command queue."""
        self.__commands_queue.put(command)


# TODO
def detect_trash():
    pass


if __name__ == '__main__':
    pioneer2 = ERS('COM10', 9600)
    try:
        # pioneer2.turn_off()
        # pioneer2.add_console_command(Command('MOVE', 6000))

        pioneer2.add_console_command(Command('MOVE', 500))
        pioneer2.add_console_command(Command('MOVE', 500))
        pioneer2.add_console_command(Command('MOVE', 500))
        pioneer2.add_console_command(Command('MOVE', 500))
        pioneer2.add_console_command(Command('MOVE', 500))
        pioneer2.add_console_command(Command('MOVE', 500))
        pioneer2.add_console_command(Command('MOVE', 500))
        pioneer2.add_console_command(Command('MOVE', 500))
        pioneer2.add_console_command(Command('MOVE', 500))
        pioneer2.add_console_command(Command('MOVE', 500))
        pioneer2.add_console_command(Command('MOVE', 500))
        pioneer2.add_console_command(Command('MOVE', 500))

        pioneer2.add_console_command(Command('EXIT', None))

        # pioneer2.add_console_command(Command('HEAD', 90))
        # pioneer2.add_console_command(Command('SETO', None))
        # pioneer2.add_console_command(Command('SONAR', 0))
        # pioneer2.add_console_command(Command('BUMP_STALL', 0))
        # pioneer2.add_console_command(Command('EXIT', None))
        pioneer2.state_machine()
        pioneer2.turn_off()

    except BaseException as e:
        print("Error during execution:", e)
    finally:
        pioneer2.turn_off()

""" TRASH
def running_command_completed(self):
    if self.sip_info['motor_status']:
        self.last_command = self.command
        self.command = None
        return False
    if not self.sip_info['motor_status'] and self.command is None:
        # Motors off. We have to verify if the last command was successfully completed
        # WARNING: The motors may be false because the execution of the command has not started yet.
        # return self.movement_completed()
        return True
    return False

"""
