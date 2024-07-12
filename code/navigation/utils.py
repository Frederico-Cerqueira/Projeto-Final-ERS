from navigation.sip_information.coordinates import update_coordinate_info
from navigation.sip_information.motors import update_motors_info
from navigation.sip_information.sonars import update_sonar_info
from computer_vision.pi_camera import get_trash_detected, trash_collected, trash_lookup
from computer_vision.take_photo import init_cam
import threading



def process_command(ers):
    """Process the command received from ers.command."""
    # Process command
    if ers.command.name == 'EXIT':
        ers.turn_off()
    # Otherwise, if the serial communication is active, attempt to send the command to the robot
    elif ers.serial_communication.is_connected():
        #print("Command to run: ", ers.command.name, ers.command.args)
        ers.send_command(ers.command.name, ers.command.args)


def detect_trash():
    """Detect if trash was found."""
    if get_trash_detected() is None:
        return False
    return get_trash_detected()


def process_sip(ers, sip):
    """Process the SIP information received from the robot.
    And updates the SIP_INFO object with the new information."""
    if len(ers.sip_info) > 0:
        for current_sip_info in ers.sip_info:
            update_sonar_info(current_sip_info['sonars'], sip.sonars)
            update_coordinate_info(current_sip_info, sip.coordinates)
            update_motors_info(current_sip_info, sip.motors)
            ers.sip_info.remove(current_sip_info)


def detect_limit(x_pos,x_lim,y_pos , y_lim, state_machine):
    """Detect if the robot has reached the limit of the map."""
    if (x_pos >= x_lim) and state_machine.lim_direction == 'front':
        return True
    if (x_pos <= 0) and state_machine.lim_direction == 'back':
        return True
    if y_pos >= y_lim:
        return True
    return False


def last_command_terminated(ers, sip):
    """Detect if the last command sent to the robot was terminated."""
    if sip.motors.on:
        ers.command = None
    if not sip.motors.on and ers.command is None:
        return True
    else:
        return False


def collect_trash():
    """calls the trash_collected function from pi_camera.py to set the trash_detected variable to False."""
    return trash_collected()


def lookup_for_trash(cam):
    """calls the trash_lookup function from pi_camera.py to capture an image and process it."""
    thread1 = threading.Thread(target=trash_lookup, args=(cam, 2))
    thread1.start()



def detected_trash():
    """calls the get_trash_detected function from pi_camera.py to retrieve the current value of the trash_detected variable."""
    return get_trash_detected()


def cam_init():
    """calls the init_cam function from pi_camera.py to initialize the camera."""
    return init_cam()
