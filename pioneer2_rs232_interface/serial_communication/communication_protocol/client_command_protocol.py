# -*- coding: utf-8 -*-

from protocol_functions import calculate_packet_checksum, calculate_int_data_bytes

"""

"""


def get_client_command_packet(command, arg):
    return __commands[command](arg)


def __sync_0(arg=None):
    """Retorna o pacote do comando de sincronização SYNC0."""
    return b'\xfa\xfb\x03\x00\x00\x00'


def __sync_1(arg=None):
    """Retorna o pacote do comando de sincronização SYNC1."""
    return b'\xfa\xfb\x03\x01\x00\x01'


def __sync_2(arg=None):
    """Retorna o pacote do comando de sincronização SYNC2."""
    return b'\xfa\xfb\x03\x02\x00\x02'


def __pulse(arg=None):
    """Retorna o pacote do comando PULSE, usado apenas para fazer reset ao watchdog dos servidores do robot."""
    return b'\xfa\xfb\x03\x00\x00\x00'


def __open(arg=None):
    """Retorna o pacote do comando OPEN, usado para iniciar os servidores do robot."""
    return b'\xfa\xfb\x03\x01\x00\x01'


def __enable(arg):
    """
    Retorna o pacote do comando ENABLE, usado para ativar e desativar os motores do robot:
        - Argumento = 1 - ativa;
        - Argumento = 0 - desativa.
    """
    # Converter argumento para bytes
    data_bytes = calculate_int_data_bytes(arg)
    # Colocar bytes do argumento no pacote
    packet = b'\xfa\xfb\x06\x04' + data_bytes + b'\x00\x00'
    # Calcular checksum do pacote
    packet_checksum = calculate_packet_checksum(packet)
    packet = packet[:-2] + packet_checksum

    return packet


def __close(arg=None):
    """Retorna o pacote do comando CLOSE, usado para desligar os servidores do robot e terminar a ligação."""
    return b'\xfa\xfb\x03\x02\x00\x02'


# TODO
def __polling(arg):
    data_bytes = calculate_int_data_bytes(arg)
    packet = b'\xfa\xfb\x06\x03' + data_bytes + b'\x00\x00'
    # Calcular checksum do pacote
    packet_checksum = calculate_packet_checksum(packet)
    packet = packet[:-2] + packet_checksum

    return packet


def __set_a(arg):
    """
    Retorna o pacote do comando SETA, usado para definir a aceleração ou desaceleração de translação do robot, em centímetros por segundo ao quadrado:
        - Argumento com valor positivo - aceleração;
        - Argumento com valor negativo - desaceleração.
    """
    # Converter argumento para bytes
    data_bytes = calculate_int_data_bytes(arg)
    # Colocar bytes do argumento no pacote
    packet = b'\xfa\xfb\x06\x05' + data_bytes + b'\x00\x00'
    # Calcular checksum do pacote
    packet_checksum = calculate_packet_checksum(packet)
    packet = packet[:-2] + packet_checksum

    return packet


# TODO: tornar todos os valores positivos
def __set_v(arg):
    """Retorna o pacote do comando SETV, usado para definir a velocidade máxima de translação do robot, em centímetros por segundo."""
    # Converter argumento para bytes
    data_bytes = calculate_int_data_bytes(arg)
    # Colocar bytes do argumento no pacote
    packet = b'\xfa\xfb\x06\x06' + data_bytes + b'\x00\x00'
    # Calcular checksum do pacote
    packet_checksum = calculate_packet_checksum(packet)
    packet = packet[:-2] + packet_checksum

    return packet


def __set_o(arg=None):
    """Retorna o pacote do comando SETO, usado para reinicializar as coordenadas na origem do robot."""
    return b'\xfa\xfb\x03\x07\x00\x07'


def __move(arg):
    """
    Retorna o pacote do comando MOVE, usado para fazer o robot deslocar-se a distância definida no argumento, em centímetros:
        - Argumento com valor positivo - deslocar-se para a frente;
        - Argumento com valor negativo - deslocar-se para trás.
    """
    # Converter argumento para bytes
    data_bytes = calculate_int_data_bytes(arg)
    # Colocar bytes do argumento no pacote
    packet = b'\xfa\xfb\x06\x08' + data_bytes + b'\x00\x00'
    # Calcular checksum do pacote
    packet_checksum = calculate_packet_checksum(packet)
    packet = packet[:-2] + packet_checksum

    return packet


def __rotate(arg):
    """
    Retorna o pacote do comando ROTATE, usado para fazer o robot rodar o número de graus definidos no argumento:
        - Argumento com valor positivo - rodar para a direita;
        - Argumento com valor negativo - rodar para a esquerda.
    """
    # Converter argumento para bytes
    data_bytes = calculate_int_data_bytes(arg)
    # Colocar bytes do argumento no pacote
    packet = b'\xfa\xfb\x06\x09' + data_bytes + b'\x00\x00'
    # Calcular checksum do pacote
    packet_checksum = calculate_packet_checksum(packet)
    packet = packet[:-2] + packet_checksum

    return packet


def __set_rv(arg):
    """Retorna o pacote do comando SETRV, usado para definir a velocidade máxima de rotação do robot, em graus por segundo."""
    # Converter argumento para bytes
    data_bytes = calculate_int_data_bytes(arg)
    # Colocar bytes do argumento no pacote
    packet = b'\xfa\xfb\x06\x0a' + data_bytes + b'\x00\x00'
    # Calcular checksum do pacote
    packet_checksum = calculate_packet_checksum(packet)
    packet = packet[:-2] + packet_checksum

    return packet


# TODO
def __vel(arg):
    pass


def __head(arg):
    """
    Retorna o pacote do comando HEAD, usado para fazer o robot rodar o número de graus definidos no argumento:
        - Argumento com valor positivo - rodar para a direita;
        - Argumento com valor negativo - rodar para a esquerda.
    """
    # Converter argumento para bytes
    data_bytes = calculate_int_data_bytes(arg)
    # Colocar bytes do argumento no pacote
    packet = b'\xfa\xfb\x06\x0c' + data_bytes + b'\x00\x00'
    # Calcular checksum do pacote
    packet_checksum = calculate_packet_checksum(packet)
    packet = packet[:-2] + packet_checksum

    return packet


def __d_head(arg):
    """
    Retorna o pacote do comando DHEAD, usado para fazer o robot rodar o número de graus definidos no argumento:
        - Argumento com valor positivo - rodar para a direita;
        - Argumento com valor negativo - rodar para a esquerda.
    """
    # Converter argumento para bytes
    data_bytes = calculate_int_data_bytes(arg)
    # Colocar bytes do argumento no pacote
    print("databytes", data_bytes)
    packet = b'\xfa\xfb\x06\x0d' + data_bytes + b'\x00\x00'
    # Calcular checksum do pacote
    packet_checksum = calculate_packet_checksum(packet)
    packet = packet[:-2] + packet_checksum

    return packet


# TODO
def __say(arg):
    pass


# TODO: testar
def __config(arg=None):
    """Retorna o pacote do comando CONFIG, usado para obter as configurações do robot."""
    return b'\xfa\xfb\x03\x12\x00\x12'

# TODO
def __encoder(arg):
    pass


def __r_vel(arg):
    """
    Retorna o pacote do comando RVEL, usado para fazer o robot rodar o número de graus definidos no argumento:
        - Argumento com valor positivo - rodar para a direita;
        - Argumento com valor negativo - rodar para a esquerda.
    """
    # Converter argumento para bytes
    data_bytes = calculate_int_data_bytes(arg)
    # Colocar bytes do argumento no pacote
    packet = b'\xfa\xfb\x06\x15' + data_bytes + b'\x00\x00'
    # Calcular checksum do pacote
    packet_checksum = calculate_packet_checksum(packet)
    packet = packet[:-2] + packet_checksum

    return packet


# TODO
def __dc_head(arg):
    pass


def __set_ra(arg):
    """
    Retorna o pacote do comando SETRA, usado para definir a acelereção ou desaceleração de rotação do robot, em graus por segundo ao quadrado:
        - Argumento com valor positivo - aceleração;
        - Argumento com valor negativo - desaceleração.
    """
    # Converter argumento para bytes
    data_bytes = calculate_int_data_bytes(arg)
    # Colocar bytes do argumento no pacote
    packet = b'\xfa\xfb\x06\x17' + data_bytes + b'\x00\x00'
    # Calcular checksum do pacote
    packet_checksum = calculate_packet_checksum(packet)
    packet = packet[:-2] + packet_checksum

    return packet


# TODO
def __sonar(arg):
    data_bytes = calculate_int_data_bytes(arg)
    packet = b'\xfa\xfb\x06\x1C' + data_bytes + b'\x00\x00'
    packet_checksum = calculate_packet_checksum(packet)
    packet = packet[:-2] + packet_checksum

    return packet


# TODO: testar
def __stop(arg):
    """
    Retorna o pacote do comando STOP, usado para parar o robot.
    O robot tem em conta o valor de desaceleração definido para parar.
    """
    return b'\xfa\xfb\x03\x29\x00\x29'


# TODO
def __digout(arg):
    pass


# TODO
def __vel_2(arg):
    pass


# TODO
def __gripper(arg):
    pass


# TODO
def __adsel(arg):
    pass


# TODO
def __gripper_val(arg):
    pass


# TODO
def __gripper_request(arg):
    pass


# TODO
def __io_request(arg):
    pass


# TODO
def __ptupos(arg):
    pass


# TODO
def __tty(arg):
    pass


# TODO
def __get_aux(arg):
    pass


# TODO
def __bump_stall(arg):
    data_bytes = calculate_int_data_bytes(arg)
    packet = b'\xfa\xfb\x06\x2C' + data_bytes + b'\x00\x00'
    packet_checksum = calculate_packet_checksum(packet)
    packet = packet[:-2] + packet_checksum

    return packet


# TODO
def __tcm2(arg):
    pass


# TODO
def __dock(arg):
    pass


# TODO
def __joy_drive(arg):
    pass


def __e_stop(arg):
    """
    Retorna o pacote do comando E_STOP, usado para parar de emergência o robot.
    Ao contrário do comando STOP, neste caso o robot não tem em conta o valor de desaceleração definido e pára o mais rápido possível.
    """
    return b'\xfa\xfb\x03\x55\x00\x55'


# TODO
def __e_stall(arg):
    pass


# TODO
def __step(arg):
    pass


# TODO
def __tty3(arg):
    pass


# TODO
def __get_aux_2(arg):
    pass


# TODO
def __rot_kp(arg):
    pass


# TODO
def __rot_kv(arg):
    pass


# TODO
def __rot_ki(arg):
    pass


# TODO
def __trans_kp(arg):
    pass


# TODO
def __trans_kv(arg):
    pass


# TODO
def __trans_ki(arg):
    pass


# TODO
def __rev_count(arg):
    pass


# TODO
def __playlist(arg):
    pass


# TODO
def __sound_toggle(arg):
    pass


""" Client Command Protocol command list """
__commands = {
    'SYNC0': __sync_0,
    'SYNC1': __sync_1,
    'SYNC2': __sync_2,
    'PULSE': __pulse,
    'OPEN': __open,
    'ENABLE': __enable,
    'CLOSE': __close,
    'SETA': __set_a,
    'SETV': __set_v,
    'SETO': __set_o,
    'MOVE': __move,
    'ROTATE': __rotate,
    'SETRV': __set_rv,
    'HEAD': __head,
    'DHEAD': __d_head,
    'CONFIG': __config,
    'RVEL': __r_vel,
    'SETRA': __set_ra,
    'STOP': __stop,
    'E_STOP': __e_stop,
    'SONAR': __sonar,
    'POLLING': __polling,
    'BUMP_STALL': __bump_stall
}
