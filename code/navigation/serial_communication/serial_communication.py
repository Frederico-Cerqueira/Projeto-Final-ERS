# -*- coding: utf-8 -*-

import serial
import time
import sys

sys.path.append("./serial_communication")
sys.path.append("./serial_communication/communication_protocol")

from navigation.serial_communication.communication_protocol.client_command_protocol import get_client_command_packet
from navigation.serial_communication.communication_protocol.server_information_protocol import get_server_information_packet

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
        """ Set the serial port to use"""
        self.__serial_connection.port = port

    def set_baudrate(self, baudrate):
        """ Set the baudrate to use"""
        self.__serial_connection.baudrate = baudrate

    def connect(self):
        """ Connect to the serial port """
        self.__serial_connection.open()
        self.__serial_connection.flushInput()
        self.__serial_connection.flushOutput()

    def disconnect(self):
        """ Disconnect from the serial port """
        self.__serial_connection.close()

    def is_connected(self):
        """ Check if the serial port is connected """
        return self.__serial_connection.is_open

    def check_sip_availability(self):
        """ Check sip availability """
        return self.__serial_connection.in_waiting > 0

    def get_sip(self):
        """ Receives and returns a SIP """
        # Receive information from the header and data_img_processing size.
        packet_header_and_size = self.__serial_connection.read(3)
        packet_size = packet_header_and_size[2]

        # Receive data_img_processing
        packet_data = self.__serial_connection.read(packet_size)

        # Concatenate the header with the data_img_processing.
        packet = packet_header_and_size + packet_data

        # Retrieve and return information from the SIP
        return get_server_information_packet(packet)

    def send_command(self, command, arg=None):
        """ Send a command to the serial port """
        if arg is not None:
            arg = int(arg)
        # Retrieve command packet.
        command_packet = self.__get_packet(command, arg)
        if command != 'PULSE':
            pass
        # Send packet
        self.__serial_connection.write(command_packet)
        # Wait 5ms between commands.
        time.sleep(0.005)

    @staticmethod
    def __get_packet(command, arg):
        return get_client_command_packet(command, arg)
