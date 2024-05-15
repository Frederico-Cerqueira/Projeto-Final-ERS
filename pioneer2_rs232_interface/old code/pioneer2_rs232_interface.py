# -*- coding: utf-8 -*-

# Define import path
import sys

sys.path.append("../serial_communication")
sys.path.append("../serial_communication/communication_protocol")

import threading
import queue
import time
import numpy as np

from datetime import datetime

from serial_communication.serial_communication import SerialCommunication
from tests.tests import *
from tests.timer import *


class Pioneer2RS232InterfaceControl:
    """Interface que permite comunicar com o robot Pioneer2 através de uma ligação série."""

    def __init__(self, port, baudrate):
        # Inicializar a interface
        self.__initialize_interface()

        # Iniciar comunicação série
        self.__start_serial_connection(port, baudrate)

        # Estabelecer ligação com o robot caso a ligação série seja iniciada
        if self.__serial_communication.is_connected():
            # Estabelecer comunicação com o robot
            self.__establish_communication()

        # Correr interface
        self.__run()

    def __initialize_interface(self):
        print("Pioneer2 RS-232 Interface - Init")
        self.__interface_running = True

        """ Configurar nova thread para receber input da consola """
        # Criar queue que vai conter os comandos recebidos pela consola
        self.__console_input_queue = queue.Queue()
        # Criar queue que vai conter os comandos a serem executados
        self.__console_commands_queue = queue.Queue()
        # Criar thread:
        #   target: define a função invocada pelo método run da thread
        #   daemon: uma thread daemon corre sem bloquear a thread principal de terminar a sua execução e
        #   se a thread principal terminar de executar, as daemon threads associadas são terminadas
        self.__input_thread = threading.Thread(target=self.__fetch_commands_from_console, daemon=True)
        # Iniciar thread
        self.__input_thread.start()

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
            self.__sip_info = sip_info_aux

    def __fetch_commands_from_console(self):
        """Função que executa numa thread paralela e guarda os comandos recebidos pela consola."""

        print('Pioneer2 RS-232 Interface - Ready for Console Input')

        # Aguardar receber comando pela consola e guarda-lo
        while self.__interface_running:
            input_command = input()
            self.__console_input_queue.put(input_command.upper())

    #############################################################################
    def __fetch_script_from_console(self):
        """Função que executa numa thread paralela e guarda os comandos recebidos no script colocado na consola."""

        print('Pioneer2 RS-232 Interface - Ready for Console Input (script)')
        input_script_path = input()
        try:
            # Open the script file
            with open(input_script_path, 'r') as script_file:
                # Read commands line by line
                for line in script_file:
                    # Remove leading/trailing whitespaces
                    command = line.strip()
                    # Skip empty lines
                    if not command:
                        continue
                    # Execute the command using subprocess
                    self.__console_input_queue.put(command)

        except FileNotFoundError:
            print(f"Error: Script file not found: {input_script_path}")
        except Exception as e:
            print(f"Error processing script: {e}")

    #############################################################################

    def __process_command(self):
        input_command = self.__console_commands_queue.get()
        print('comando recebido')
        print(input_command)
        print("Command Received: {}".format(input_command))
        # TODO: indicar à queue que o comando foi processado
        self.__console_commands_queue.task_done()

        input_command = input_command.split()

        if input_command[0] == 'T':
            commands = test_movement_translation(input_command[1], input_command[2], input_command[3])

            for command in commands:
                print(command)

                command = command.split()
                command_str = command[0]
                arg = None
                if len(command) > 1:
                    arg = int(command[1])

                self.__send_command('PULSE')
                self.__send_command(command_str, arg)

            self.__start_time.set_waiting(True)

        elif input_command[0] == 'R':
            commands = test_movement_rotation(input_command[1], input_command[2], input_command[3])

            for command in commands:
                print(command)

                command = command.split()
                command_str = command[0]
                arg = None
                if len(command) > 1:
                    arg = int(command[1])

                self.__send_command('PULSE')
                self.__send_command(command_str, arg)

            self.__start_time.reset()

        # Printar último SIP
        elif input_command[0] == 'S':
            print(self.__sip_info)

        # Se o comando for para desligar a interface
        elif input_command[0] == 'EXIT':
            # Enviar comandos ao robot para desligar
            if self.__serial_communication.is_connected():
                # Desligar os motores do robot
                self.__send_command('ENABLE', 0)

                # Terminar ligação com o robot
                self.__send_command('CLOSE')

                # Terminar ligação série
                self.__serial_communication.disconnect()

            self.__interface_running = False

        # Caso contrário, e se a comunicação série estiver ativa, tentar enviar o comando ao robot
        elif self.__serial_communication.is_connected():
            arg = None
            if len(input_command) > 1:
                arg = int(input_command[1])

            self.__send_command(input_command[0], arg)

    def __send_command(self, command, arg=None):
        self.__serial_communication.send_command(command, arg)

    #####################################################################
    def __get_new_command(self):
        new_command = self.__console_input_queue.get()
        print('este é o novo comando')
        print(new_command)
        self.__console_commands_queue.put(new_command)

    #####################################################################

    def __run(self):
        print("Pioneer2 RS-232 Interface - Running")

        # Tempo inicial
        tempo_init = tempo_fin = datetime.now().timestamp()

        # Tempo Pulse inicial
        tempo_pulse_inicial = datetime.now().timestamp()
        tempo_pulse_final = datetime.now().timestamp()

        # Contar tempo no inicio do movimento até ao final do movimento
        self.__start_time = Timer()

        # Inicialização da variável que vai conter o dicionario com a informação do SIP
        self.__sip_info = None
        posicao_inicial = 0
        x_pos_robot_inicial = 0
        y_pos_robot_inicial = 0

        while self.__interface_running:
            # Verificar se existe um pacote SIP no canal
            if self.__serial_communication.check_sip_availibility() and (tempo_fin - tempo_init > 0.100):
                tempo_init = datetime.now().timestamp()
                # Processar SIP
                self.__process_sip()

            # Caso exista informação de um SIP
            if self.__sip_info is not None:
                print(self.__sip_info['motor_status'])
                if (self.__sip_info['motor_status'] == False):
                    print("DEU FALSE")
                    self.__get_new_command()

                # Caso esteja a começar um teste, guardar posição inicial do robô e iniciar contador
                if self.__start_time.get_is_waiting() and self.__start_time.get_is_counting() == False and \
                        self.__sip_info['motor_status']:
                    x_pos_robot_inicial = self.__sip_info['x_pos']
                    y_pos_robot_inicial = self.__sip_info['y_pos']
                    posicao_inicial = np.array([x_pos_robot_inicial, y_pos_robot_inicial])

                    self.__start_time.start()

                # Caso esteja a terminar um teste, guardar posição final do robô e printar info
                if self.__start_time.get_is_waiting() == False and self.__start_time.get_is_counting() and \
                        self.__sip_info['motor_status'] == False:
                    x_pos_robot_final = self.__sip_info['x_pos']
                    y_pos_robot_final = self.__sip_info['y_pos']
                    posicao_final = np.array([x_pos_robot_final, y_pos_robot_final])

                    x_pos_total = x_pos_robot_final - x_pos_robot_inicial
                    y_pos_total = y_pos_robot_final - y_pos_robot_inicial

                    distancia_percorrida = np.linalg.norm(posicao_final - posicao_inicial)

                    stop_time = self.__start_time.stop()

                    print("Tempo total ", stop_time)
                    print("Movimento em X ", x_pos_total)
                    print("Movimento em Y ", y_pos_total)
                    print("Posição em X ", self.__sip_info['x_pos'])
                    print("Posição em Y ", self.__sip_info['y_pos'])
                    print("Posição Heading ", self.__sip_info['th_pos'])
                    print("distancia_percorrida ", distancia_percorrida)
                    print("Velocidade Calculada ", distancia_percorrida / stop_time)

            # Verificar se existem comandos da consola
            if self.__console_commands_queue.qsize() > 0:
                self.__process_command()

                # Sair do while loop se o EXIT for recebido
                if not self.__interface_running:
                    break

            # Manter robot acordado
            if self.__serial_communication.is_connected() and (tempo_pulse_final - tempo_pulse_inicial > 1.500):
                tempo_pulse_inicial = datetime.now().timestamp()
                self.__serial_communication.send_command('PULSE')

            # Calcular tempos para criar um setinterval
            # Tempo final loop
            tempo_fin = datetime.now().timestamp()
            tempo_pulse_final = datetime.now().timestamp()
            time.sleep(0.001)

        print("Pioneer2 RS-232 Interface - Shutdown")


# Correr interface
if __name__ == '__main__':
    # pioneer2_rs232_interface = Pioneer2RS232InterfaceControl("COM3", 9600)
    pioneer2_rs232_interface = Pioneer2RS232InterfaceControl('COM6', 9600)
