# -*- coding: utf-8 -*-

# Define import path
import sys
# sys.path.append("../serial_communication")

import serial
# import serial.tools.list_ports
from datetime import datetime
import time


# from serial_communication.serial_communication import SerialCommunication


def init():
    pass
    # serial_communication = SerialCommunication('/dev/ttyUSB0', 9600)

    # serial_communication = serial.Serial('/dev/ttyUSB0', 9600, timeout=10)
    #
    # serial_communication.write(b'\xfa\xfb\x03\x00\x00\x00')
    # serial_communication.write(b'\xfa\xfb\x03\x01\x00\x01')
    # serial_communication.write(b'\xfa\xfb\x03\x02\x00\x02')
    #
    # info = serial_communication.read(100)
    #
    # print(info)


def process_command(command):
    pass


def get_packet(serial_communication):
    packet_header_and_size = serial_communication.read(3)
    packet_size = packet_header_and_size[2]

    packet_data = serial_communication.read(packet_size)
    time.sleep(0.005)

    packet = packet_header_and_size + packet_data

    # print(packet)

    return packet


def calculate_int_data_bytes(value):
    argument_type = b'\x3b'  # Inteiro positivo

    # Inteiro negativo
    if value < 0:
        value = abs(value)  # Inteiros negativos são transmitidos como o seu valor absoluto
        argument_type = b'\x1b'

    return argument_type + value.to_bytes(2, byteorder='little', signed=True)


# def get_packet():
#     # Ler serial
#     packet = b'\xfa\xfb\x90\x01\x02\x03\x04\xfe\xff'
#
#     # Calcular checksum
#     calculated_checksum = calculate_checksum(packet)
#
#     if packet[-2:] == calculated_checksum:
#         pass


def calculate_checksum(packet):
    n_bites = packet[
                  0] - 2  # vai buscar o primeiro byte que irá dar o tamanho do packet e é retirado os bytes de checksum
    index = 0  # é necessário para percorrer o array de bytes
    packet_no_checksum = packet[1:-2]  # retira o checksum inicial que ira ser igual a zero
    checksum = 0  # inicializa o checksum a zero

    while (index < n_bites - 1):
        checksum += packet_no_checksum[index] << 8 | packet_no_checksum[index + 1]  # vai buscar ao array uma word
        checksum = checksum & int.from_bytes(b'\xff\xff', "little")  # faz a operação & com o checksum anterior
        index += 2  # aumenta o indice do array para ir buscar a próxima word

    if (n_bites > index):  # caso seja impar é feita a operação XOR com o último byte do array
        checksum = checksum ^ packet_no_checksum[n_bites - 1]

    return checksum


def test_calculate_checksum(packet):
    n_bites = packet[
                  0] - 2  # Vai buscar o primeiro byte que irá dar o tamanho do packet e é retirado os bytes de checksum
    index = 0  # É necessário para percorrer o array de bytes
    packet_no_checksum = packet[1:-2]  # retira o checksum inicial que ira ser igual a zero
    checksum = 0  # inicializa o checksum a zero

    while (index < n_bites - 1):
        checksum += packet_no_checksum[index] << 8 | packet_no_checksum[index + 1]  # vai buscar ao array uma word
        checksum = checksum & int.from_bytes(b'\xff\xff',
                                             byteorder="little")  # faz a operação & com o checksum anterior
        index += 2  # aumenta o indice do array para ir buscar a próxima word

    if (n_bites > index):  # caso seja impar é feita a operação XOR com o último byte do array
        checksum = checksum ^ packet_no_checksum[n_bites - 1]

    checksum = checksum.to_bytes(2, byteorder='big')

    return checksum


def validate_checksum(packet):
    packet = b'\xfa\xfb\x90\x01\x02\x03\x04\xfe\xff'

    packet_data = packet[2:-2]
    packet_checksum = packet[-2:]

    validation = packet_data ^ packet_checksum

    if validation == b'\0x00':
        return True
    else:
        return False


def robot_set_motion_settings(serial_communication, vmax, vacc):
    # SETV
    data_bytes = calculate_int_data_bytes(vmax)
    command = b'\xfa\xfb\x06\x06' + data_bytes + b'\x00\x00'
    command_checksum = test_calculate_checksum(command[2:])
    command = command[:-2] + command_checksum
    serial_communication.write(command)
    time.sleep(0.1)
    # SETAcc
    data_bytes = calculate_int_data_bytes(vacc)
    command = b'\xfa\xfb\x06\x05' + data_bytes + b'\x00\x00'
    command_checksum = test_calculate_checksum(command[2:])
    command = command[:-2] + command_checksum
    serial_communication.write(command)
    time.sleep(0.1)
    # SETDec
    data_bytes = calculate_int_data_bytes(-vacc)
    command = b'\xfa\xfb\x06\x05' + data_bytes + b'\x00\x00'
    command_checksum = test_calculate_checksum(command[2:])
    command = command[:-2] + command_checksum
    serial_communication.write(command)
    time.sleep(0.1)


# Correr script
if __name__ == '__main__':
    # packet_data = b'\xfa\xfb\x90\x01\x02\x03\x04\xfe\xff'
    # packet_data = b'\x32\x33'

    # print( int.from_bytes(packet_data[3:5], byteorder='big') )
    # print(packet_data[-2:])
    # print(packet_data[1])
    # print(int.from_bytes(packet_data[3:5], byteorder='big'))

    # Obter lista de portas série
    # print( list(serial.tools.list_ports.comports()) )

    # Esperar selecção de porta
    # port_name = input("Seleccione uma porta: ")

    # serial_communication = serial.Serial("COM3", 9600, timeout=5)
    # serial_communication.set_buffer_size(rx_size=12800, tx_size=12800)

    Vmax = 0
    Vacc = 0
    DistCMD = 0

    serial_communication = serial.Serial('COM6', 9600, timeout=10)
    time.sleep(0.1)
    serial_communication.flushInput()
    serial_communication.flushOutput()

    serial_communication.write(b'\xfa\xfb\x03\x00\x00\x00')  # SYNC0
    time.sleep(0.1)
    serial_communication.write(b'\xfa\xfb\x03\x01\x00\x01')  # SYNC1
    time.sleep(0.1)
    serial_communication.write(b'\xfa\xfb\x03\x02\x00\x02')  # SYNC2
    time.sleep(0.1)
    serial_communication.write(b'\xfa\xfb\x03\x01\x00\x01')  # OPEN - iniciar servidores do robot
    time.sleep(0.1)
    open_command_sent = True
    serial_communication.write(b'\xfa\xfb\x03\x00\x00\x00')  # PULSE
    time.sleep(0.1)

    """ Testar comandos """
    # Ligar motores do robot (enviar ENABLE = 1)
    data_bytes = calculate_int_data_bytes(1)
    enable_1_command = b'\xfa\xfb\x06\x04' + data_bytes + b'\x00\x00'
    enable_1_command_checksum = test_calculate_checksum(enable_1_command[2:])
    enable_1_command = enable_1_command[:-2] + enable_1_command_checksum
    serial_communication.write(enable_1_command)
    time.sleep(0.1)

    # Enviar comandos para configurar velocidades e acelerações
    # robot_set_motion_settings(serial_communication, Vmax, Vacc)

    # Mandar andar robot (enviar MOVE com o valor dos mm)
    # data_bytes = calculate_int_data_bytes(DistCMD)
    # translate_command = b'\xfa\xfb\x06\x08' + data_bytes + b'\x00\x00'
    # translate_command_checksum = test_calculate_checksum(translate_command[2:])
    # rotate_command = translate_command[:-2] + translate_command_checksum
    # serial_communication.write(rotate_command)
    # time.sleep(0.1)

    # Rodar robot (enviar ROTATE com o valor dos graus)
    # data_bytes = calculate_int_data_bytes(300)
    # rotate_command = b'\xfa\xfb\x06\x08' + data_bytes + b'\x00\x00'
    # rotate_command_checksum = test_calculate_checksum(rotate_command[2:])
    # rotate_command = rotate_command[:-2] + rotate_command_checksum
    # serial_communication.write(rotate_command)
    # time.sleep(0.1)

    # Rodar robot (enviar ROTATE com o valor dos graus)
    # data_bytes = calculate_int_data_bytes(10)
    # rotate_command = b'\xfa\xfb\x06\x09' + data_bytes + b'\x00\x00'
    # rotate_command_checksum = test_calculate_checksum(rotate_command[2:])
    # rotate_command = rotate_command[:-2] + rotate_command_checksum
    # serial_communication.write(rotate_command)
    # time.sleep(0.1)

    # Rodar robot (enviar RVEL com o valor dos graus/s)
    # data_bytes = calculate_int_data_bytes(5)
    # rotate_command = b'\xfa\xfb\x06\x21' + data_bytes + b'\x00\x00'
    # rotate_command_checksum = test_calculate_checksum(rotate_command[2:])
    # rotate_command = rotate_command[:-2] + rotate_command_checksum
    # serial_communication.write(rotate_command)
    # time.sleep(0.1)

    times = []
    sips = []
    tempo_inicial = datetime.now().timestamp()
    tempo = datetime.now().timestamp()

    tempo_while = 0
    tempo_pulse = 0
    while len(times) <= 100 and tempo_while <= 10 and serial_communication.is_open:
        serial_communication.write(b'\xfa\xfb\x03\x00\x00\x00')  # PULSE
        time.sleep(0.1)

        # Enviar comando OPEN
        # if len(sips) == 3 and not open_command_sent:
        #     print("Open command sent")
        #     serial_communication.write(b'\xfa\xfb\x03\x01\x00\x01')
        #     time.sleep(0.1)
        #     open_command_sent = True
        #
        #     tempo = datetime.now().timestamp()

        # Enviar comando PULSE
        # if open_command_sent and tempo_pulse > 1:
        #     serial_communication.write(b'\xfa\xfb\x03\x01\x00\x01')

        # Verificar se existe algo a receber
        if serial_communication.in_waiting > 0:
            info = get_packet(serial_communication)

            # Guardar SIPS (após comando OPEN)
            if open_command_sent:
                tempo_espera = (datetime.now().timestamp() - tempo) * 1000.0
                times.append(tempo_espera)

                sips.append({
                    "sip": info,
                    "tempo": tempo_espera
                })

                tempo = datetime.now().timestamp()

        tempo_while = datetime.now().timestamp() - tempo_inicial

    tempo_total = (datetime.now().timestamp() - tempo_inicial) * 1000.0

    # Desligar motores do robot (enviar ENABLE = 0)
    enable_0_command = b'\xfa\xfb\x06\x04\x3b\x00\x00\x00\x00'
    enable_0_command_checksum = test_calculate_checksum(enable_0_command[2:])
    enable_0_command = enable_0_command[:-2] + enable_0_command_checksum
    serial_communication.write(enable_0_command)
    time.sleep(0.1)

    # Terminar ligação com o robot (enviar CLOSE)
    serial_communication.write(b'\xfa\xfb\x03\x02\x00\x02')
    time.sleep(0.1)
    serial_communication.close()

    print("Tempos em milisegundos: ", times)
    print("Valor mínimo: ", str(round(min(times), 2)), " milisegundos")
    print("Valor máximo: ", str(round(max(times), 2)), " milisegundos")
    print("Valor médio: ", str(round((sum(times)) / (len(times) * 1.0), 2)), " milisegundos")
    print("Tempo total: ", str(round(tempo_total / 1000.0, 2)), " segundos")

    print("SIPS: ", sips)
