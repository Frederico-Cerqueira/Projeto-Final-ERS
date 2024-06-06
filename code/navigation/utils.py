from sip_information.coordinates import update_coordinate_info
from sip_information.motors import update_motors_info
from sip_information.sonars import update_sonar_info, print_sonar_info


def process_command(ers):
    # Process command
    if ers.command.name == 'EXIT':
        ers.turn_off()
    # Otherwise, if the serial communication is active, attempt to send the command to the robot
    elif ers.serial_communication.is_connected():
        print("Command to run: ", ers.command.name, ers.command.args)
        ers.send_command(ers.command.name, ers.command.args)


# TODO
def detect_trash():
    # Chamar função da visão que deteta o lixo e retorna true or false
    return False


def process_sip(ers, sip):
    if len(ers.sip_info) > 0:
        for current_sip_info in ers.sip_info:
            update_sonar_info(current_sip_info['sonars'], sip.sonars)
            update_coordinate_info(current_sip_info, sip.coordinates)
            update_motors_info(current_sip_info, sip.motors)
            ers.sip_info.remove(current_sip_info)


def detect_limit():
    return False


"""
def detect_limit(x_pos, x_lim, y_pos, y_lim):
    error = 7
    x_maximo_range = range(x_lim - error, x_lim + error)
    x_minumum_range = range(0 - error, 0 + error)
    y_maximo_range = range(y_lim - error, y_lim + error)
    y_minumum_range = range(0 - error, 0 + error)
    if (x_pos in x_maximo_range or x_pos in x_minumum_range or
            y_pos in y_maximo_range or y_pos in y_minumum_range):
        return True
    return False
"""


def last_command_terminated(ers, sip):
    if sip.motors.on:
        ers.command = None
    if not sip.motors.on and ers.command is None:
        return True
    else:
        return False
