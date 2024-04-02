# -*- coding: utf-8 -*-

import serial
from communication_protocol.client_command_protocol import get_client_command_packet
from communication_protocol.server_information_protocol import get_server_information_packet
import time

"""

"""


class SerialCommunication:
    def __init__(self, port, baudrate):
        # Setup serial connection
        self.__serial_connection = serial.Serial(timeout=0)

        self.set_port(port)
        self.set_baudrate(baudrate)

        self.connect()

    def set_port(self, port):
        self.__serial_connection.port = port

    def set_baudrate(self, baudrate):
        self.__serial_connection.baudrate = baudrate

    def connect(self):
        self.__serial_connection.open()

        self.__serial_connection.flushInput()
        self.__serial_connection.flushOutput()

    def disconnect(self):
        self.__serial_connection.close()

    def is_connected(self):
        return self.__serial_connection.is_open

    def check_sip_availibility(self):
        return self.__serial_connection.in_waiting > 0

    def get_sip(self):
        # Receber informação do header e tamanho dos dados
        packet_header_and_size = self.__serial_connection.read(3)
        packet_size = packet_header_and_size[2]
       
        # Receber dados
        packet_data = self.__serial_connection.read(packet_size)
        # Juntar header aos dados
        packet = packet_header_and_size + packet_data

        # Obter e retornar informação do SIP
        return get_server_information_packet(packet)
    
    def send_command(self, command, arg=None):
        if arg != None:
            arg = int(arg)
        # Obter pacote do comando
        command_packet = self.__get_packet(command, arg)
        if command != 'PULSE':
            print(command_packet)
        # Enviar pacote
        self.__serial_connection.write(command_packet)
        # Esperar 5ms entre comandos
        time.sleep(0.005)

    @staticmethod
    def __get_packet(command, arg):
        return get_client_command_packet(command, arg)
