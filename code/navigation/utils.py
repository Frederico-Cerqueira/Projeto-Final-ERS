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


def detect_limit(x_pos, x_lim, y_pos, y_lim, state_machine):
    print("x_pos: ", x_pos, " x_lim: ", x_lim)
    if (x_pos >= x_lim and state_machine.sentido == 'front'):
        print("Sentido: ", state_machine.sentido)
        state_machine.sentido = 'back'
        return True
    if (x_pos <= 0 and state_machine.sentido == 'back'):
        print("Sentido: ", state_machine.sentido)
        state_machine.sentido = 'front'
        return True

    return False


def last_command_terminated(ers, sip):
    if sip.motors.on:
        ers.command = None
    if not sip.motors.on and ers.command is None:
        return True
    else:
        return False
