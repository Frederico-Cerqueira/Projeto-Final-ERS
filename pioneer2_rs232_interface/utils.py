from datetime import datetime

from computer_vision.main import there_is_trash
from pioneer2_rs232_interface.command import Command


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


def detect_trash():
    return there_is_trash()

#TODO
def detect_limit():
    # Fazer pedido HTTP para obter os limites
    # Através dos (x,y) e com os limites calcular se estou no limite
    pass


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
