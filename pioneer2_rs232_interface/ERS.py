# -*- coding: utf-8 -*-

# Define import path
import sys

sys.path.append("./serial_communication")
sys.path.append("./serial_communication/communication_protocol")

import threading
import queue
import time
import numpy as np

from datetime import datetime

from serial_communication.serial_communication import SerialCommunication
from tests.tests import *
from tests.timer import *
from dataclasses import dataclass


class ERS:
    """ Interface que permite comunicar com o robô Pioneer2 através de uma ligação série."""

    def __init__(self, port, baudrate):

        # Iniciar comunicação série
        self.sip_info = None
        self.__start_serial_connection(port, baudrate)

        # Estabelecer ligação com o robot caso a ligação série seja iniciada
        if self.__serial_communication.is_connected():
            # Estabelecer comunicação com o robot
            self.__establish_communication()

    def __start_serial_connection(self, port, baudrate):
        self.__serial_communication = SerialCommunication(port, baudrate)

    def __establish_communication(self):
        """Sincronizar comunicação com o robot e iniciar os servidores e motores do robot."""
        print("Pioneer2 RS-232 Interface - Establishing Connection")

        # Enviar os pacotes de sincronização e obter as respostas a cada um
        # TODO: Obter as respostas à sincronização - a resposta ao SYNC2 contém as configurações do robot (nome, etc)
        self.__send_command('SYNC0')
        self.__send_command('SYNC1')
        self.__send_command('SYNC2')

        # Iniciar os servidores do robot
        self.__send_command('OPEN')

        # Pedir o SIP de configuração
        self.__send_command('CONFIG')

        # Ligar os motores do robot
        self.__send_command('ENABLE', 1)

        # Reset da origem do robot
        self.__send_command('SETO')

    def __process_sip(self):
        sip_info_aux = self.__serial_communication.get_sip()

        if sip_info_aux is not None:
            self.sip_info = sip_info_aux

    def __process_command(self, command):
        print('Command received:' + command.comando + ' ' + str(command.args))

        # Printar último SIP
        if command.comando == 'S':
            print(self.sip_info)

        # Se o comando for para desligar a interface
        elif command.comando == 'EXIT':
            self.turn_off()
            self.__interface_running = False

        # Caso contrário, e se a comunicação série estiver ativa, tentar enviar o comando ao robot
        elif self.__serial_communication.is_connected():
            self.__send_command(command.comando, command.args)

    def __send_command(self, command, arg=None):
        self.__serial_communication.send_command(command, arg)

    def turn_off(self):
        if self.__serial_communication.is_connected():
            # Desligar os motores do robot
            self.__send_command('ENABLE', 0)

            # Terminar ligação com o robot
            self.__send_command('CLOSE')

            # Terminar ligação série
            self.__serial_communication.disconnect()

    def run(self, command_list):
        print("Pioneer2 RS-232 Interface - Running")
        self.__interface_running = True

        # Tempo inicial
        tempo_init = tempo_fin = datetime.now().timestamp()

        # Tempo Pulse inicial
        tempo_pulse_inicial = datetime.now().timestamp()
        tempo_pulse_final = datetime.now().timestamp()

        # Inicialização da variável que vai conter o dicionario com a informação do SIP
        #self.__sip_info = None

        while self.__interface_running:
            # Verificar se existe um pacote SIP no canal
            if self.__serial_communication.check_sip_availibility() and (tempo_fin - tempo_init > 0.100):
                tempo_init = datetime.now().timestamp()
                # Processar SIP
                self.__process_sip()

            # Caso exista informação de um SIP
            if self.sip_info is not None:
                print(self.sip_info['motor_status'])
                print("Posição em X ", self.sip_info['x_pos'])
                print("Posição em Y ", self.sip_info['y_pos'])
                print("Posição Heading ", self.sip_info['th_pos'])
                if not self.sip_info['motor_status']:
                    print("SIP false")

            for command in command_list:
                self.__process_command(command)

                # Sair do while loop se o EXIT for recebido
                if not self.__interface_running:
                    break

            # Manter robot acordado
            if self.__serial_communication.is_connected() and (tempo_pulse_final - tempo_pulse_inicial > 1.500):
                tempo_pulse_inicial = datetime.now().timestamp()
                self.__serial_communication.send_command('PULSE')

            # Calcular tempos para criar um setinterval
            # Tempo final 'loop'
            tempo_fin = datetime.now().timestamp()
            tempo_pulse_final = datetime.now().timestamp()
            time.sleep(0.001)

        print("Pioneer2 RS-232 Interface - Shutdown")


class Command:
    def __init__(self, comando, args):
        self.comando = comando
        self.args = args


# Correr interface
if __name__ == '__main__':
    pioneer2 = ERS('COM6', 9600)
    try:
        my_command_list = [Command('MOVE', 1000), Command('S', None), Command('EXIT', None)]
        pioneer2.run(my_command_list)
        print("Posição em X ", pioneer2.sip_info['x_pos'])
        print("Posição em Y ", pioneer2.sip_info['y_pos'])
        print("Posição Heading ", pioneer2.sip_info['th_pos'])
    except:
        print("Erro na execução")
        pioneer2.turn_off()
