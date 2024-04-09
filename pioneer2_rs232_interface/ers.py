# -*- coding: utf-8 -*-

# Define import path
import sys

from command import Command

sys.path.append("./serial_communication")
sys.path.append("./serial_communication/communication_protocol")
import queue
import time

from serial_communication.serial_communication import SerialCommunication
from tests.timer import *


class ERS:
    """ Class que permite comunicar com o robô Pioneer2 através de uma ligação série."""

    def __init__(self, port, baudrate):

        self.__running_flag = None
        self.sip_info = None
        self.__command = None
        self.verdadeiro = False

        # Iniciar comunicação série
        self.__start_serial_connection(port, baudrate)

        # Criar queue que vai conter os comandos a serem executados
        self.__commands_queue = queue.Queue()

        self.__execute_commands_queue = queue.Queue()

        # Estabelecer ligação com o robô caso a ligação série seja iniciada
        if self.__serial_communication.is_connected():
            # Estabelecer comunicação com o robô
            self.__establish_communication()

    def __start_serial_connection(self, port, baudrate):
        self.__serial_communication = SerialCommunication(port, baudrate)

    def __establish_communication(self):
        """Sincronizar comunicação com o robô e iniciar os servidores e motores do robô."""
        print("Pioneer2 RS-232 Interface - Establishing Connection")

        # Enviar os pacotes de sincronização e obter as respostas a cada um
        self.__send_command('SYNC0')
        self.__send_command('SYNC1')
        self.__send_command('SYNC2')

        # Iniciar os servidores do robô
        self.__send_command('OPEN')

        # Pedir o SIP de configuração
        self.__send_command('CONFIG')

        # Ligar os motores do robô
        self.__send_command('ENABLE', 1)

        # Reset da origem do robô
        self.__send_command('SETO')

    def add_console_command(self, command):
        """Adiciona um comando à fila de comandos da consola."""
        self.__commands_queue.put(command)

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
            self.__running_flag = False

        # Caso contrário, e se a comunicação série estiver ativa, tentar enviar o comando ao robô
        elif self.__serial_communication.is_connected():
            self.__send_command(command.comando, command.args)

    def __process_sucessive_commands(self):
        if self.sip_info is None and self.__command is None:  # 1º comando
            self.__command = self.__commands_queue.get()
            print("Executar 1º Comando")
            self.__process_command(self.__command)
        else:
            if self.sip_info['motor_status']:  # 1ª fase de acabar
                self.__command = None
            if not self.sip_info['motor_status'] and self.__command is not None:  # 2ª fase de acabar
                self.__command = self.__commands_queue.get()
                print("Executar 2º Comando")
                self.__process_command(self.__command)

    def __send_command(self, command, arg=None):
        self.__serial_communication.send_command(command, arg)

    def turn_off(self):
        if self.__serial_communication.is_connected():
            # Desligar os motores do robô
            self.__send_command('ENABLE', 0)

            # Terminar ligação com o robô
            self.__send_command('CLOSE')

            # Terminar ligação série
            self.__serial_communication.disconnect()

    def run(self):
        print("Pioneer2 RS-232 Interface - Running")
        self.__running_flag = True

        # Tempo inicial
        tempo_init = tempo_fin = datetime.now().timestamp()

        # Tempo Pulse inicial
        tempo_pulse_inicial = datetime.now().timestamp()
        tempo_pulse_final = datetime.now().timestamp()

        while True:  # (self.__commands_queue.qsize() > 0 and not self.sip_info['motor_status'])or self.__running_flag:
            print("Tamanho:", self.__commands_queue.qsize())

            if self.__serial_communication.check_sip_availibility() and (tempo_fin - tempo_init > 0.100):
                tempo_init = datetime.now().timestamp()
                self.__process_sip()

            if self.sip_info is not None:
                if not self.sip_info['motor_status']:
                    print("SIP Motor Status: False")
                else:
                    print("SIP Motor Status: True")

            self.__process_sucessive_commands()

            # Manter robô acordado
            if self.__serial_communication.is_connected() and (tempo_pulse_final - tempo_pulse_inicial > 1.500):
                tempo_pulse_inicial = datetime.now().timestamp()
                self.__serial_communication.send_command('PULSE')

            # Calcular tempos para criar um setinterval
            # Tempo final 'loop'
            tempo_fin = datetime.now().timestamp()
            tempo_pulse_final = datetime.now().timestamp()
            time.sleep(0.001)

        print("Pioneer2 RS-232 Interface - Shutdown")


def current_position(pioneer):
    print("Posição em X:", pioneer.sip_info['x_pos'])
    print("Posição em Y:", pioneer.sip_info['y_pos'])
    print("Posição Heading:", pioneer.sip_info['th_pos'])


if __name__ == '__main__':
    pioneer2 = ERS('COM6', 9600)
    try:
        pioneer2.add_console_command(Command('MOVE', 1000))
        pioneer2.add_console_command(Command('MOVE', 1000))
        pioneer2.add_console_command(Command('MOVE', 1000))
        # pioneer2.add_console_command(Command('HEAD', 90))
        # pioneer2.add_console_command(Command('SETO', None))
        # pioneer2.add_console_command(Command('SONAR', 0))
        # pioneer2.add_console_command(Command('BUMP_STALL', 1))
        # pioneer2.add_console_command(Command('EXIT', None))
        pioneer2.run()
        pioneer2.turn_off()
        current_position(pioneer2)
    except:
        print("Erro na execução")
        pioneer2.turn_off()
