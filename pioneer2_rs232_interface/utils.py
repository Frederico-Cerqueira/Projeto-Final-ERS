from datetime import datetime

from state_machine import States


def process_command(ers):
    # Set initial coordinates
    if ers.sip_info is not None:
        ers.initial_coordinates['x_pos'] = ers.sip_info['x_pos']
        ers.initial_coordinates['y_pos'] = ers.sip_info['y_pos']
        ers.initial_coordinates['th_pos'] = ers.sip_info['th_pos']
    # Process command
    if ers.command.name == 'EXIT':
        ers.turn_off()
    # Otherwise, if the serial communication is active, attempt to send the command to the robot
    elif ers.__serial_communication.is_connected():
        ers.__serial_communication.send_command(ers.command.name, ers.command.args)


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


def last_command_terminated(ers):
    if ers.sip_info['motor_status']:
        if ers.command is not None:
            print("Saving current command")
            ers.last_command = ers.command
            ers.command = None
    else:
        if ers.command is None:
            return True
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