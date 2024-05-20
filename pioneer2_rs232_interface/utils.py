from datetime import datetime

from pioneer2_rs232_interface.sip_information.coordinates import update_coordinate_info
from pioneer2_rs232_interface.sip_information.sonars import update_sonar_info
from state_machine import States


def process_command(ers):
    # Process command
    if ers.command.name == 'EXIT':
        ers.turn_off()
    # Otherwise, if the serial communication is active, attempt to send the command to the robot
    elif ers.serial_communication.is_connected():
        ers.send_command(ers.command.name, ers.command.args)


# TODO
def detect_trash():
    # Chamar função da visão que deteta o lixo e retorna true or false
    return False


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


def last_command_terminated(ers, sip):
    if sip.motors.on:
        print("motors on")
        ers.command = None
    if not sip.motors.on and ers.command is None:
        print("motors off")
        print("true")
        return True
    else:
        print("false")
        return False


def get_sip_for_change_direction(ers, state_machine):
    initial = ers.init_time_sip
    current = datetime.now().timestamp()
    if ers.__serial_communication.check_sip_availability() and (current - initial > 0.100):
        ers.init_time_sip = datetime.now().timestamp()
        sip_info_aux = ers.__serial_communication.get_sip()
        if sip_info_aux != ers.sip_info:
            ers.sip_info = sip_info_aux
            state_machine.state = States.E6a2


def process_sip(ers, sip):
    for current_sip_info in ers.sip_info:
        update_sonar_info(current_sip_info['sonars'], sip.sonars)
        update_coordinate_info(current_sip_info, sip.coordinates)
        ers.sip_info.remove(current_sip_info)