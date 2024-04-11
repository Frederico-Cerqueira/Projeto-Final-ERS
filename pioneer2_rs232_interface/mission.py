import datetime
import time

from command import Command
from ers import ERS


def area(height, width, side_distance):
    """ Sendo ql a quantidade de linhas retas que o robô percorre no eixo y,
        qc a quantidade de linhas retas que o robô percorre no eixo x,
        w (width) a largura da area, d (side_distance) a distancia lateral do movimento no eixo x
        ql = w/d + 1 -> sendo w multiplo de d, ql é um inteiro
        qc = w/d -> sendo w multiplo de d, qc é um inteiro
        É necessario ql mov(height) + qc mov(side_distance). Se qc/2 for inteiro,
        é necessário qc head(direita) + qc head (esquerda), senão
        é necessário qc + 1 head(direita) + qc head (esquerda)"""
    command_list = []
    ql = width // side_distance + 1
    qc = width // side_distance
    print(qc)

    for value in range(1, ql):
        if value % 2 == 0:
            command_list.append(Command('MOVE', height))  #"move height")
            command_list.append(Command('HEAD', 90))  #"turn left")
            command_list.append(Command('MOVE', side_distance))  #"move side")
            command_list.append(Command('HEAD', 90))  #"turn left")
        else:
            command_list.append(Command('MOVE', height))  #"move height")
            command_list.append(Command('HEAD', -90))  #"turn right")
            command_list.append(Command('MOVE', side_distance))  #"move side")
            command_list.append(Command('HEAD', -90))  #"turn right")
    command_list.append(Command('MOVE', height))  #"move height")
    return command_list


def test_par():
    height = 3
    width = 12
    side_distance = 4
    commands = area(height, width, side_distance)
    print(commands)


def test_impar():
    height = 10
    width = 9
    side_distance = 3
    commands = area(height, width, side_distance)
    print(commands)


def schedule(start_time, end_time, mission):
    """Esta função tem um problema, realiza uma espera ativa, ou seja, fica em 'loop' até
    que o tempo atual seja igual ao tempo de início da missão"""

    # Converte as horas de início e fim para objetos datetime
    start_datetime = datetime.datetime.strptime(start_time, '%H:%M')
    end_datetime = datetime.datetime.strptime(end_time, '%H:%M')

    current_datetime = datetime.datetime.now()
    while current_datetime < start_datetime:
        time.sleep(1)
        print("Waiting for the mission to start")
        current_datetime = datetime.datetime.now()

    while current_datetime <= end_datetime:
        mission()
        time.sleep(1)
        current_datetime = datetime.datetime.now()


if __name__ == '__main__':
    test_impar()
    # pioneer2_rs232_interface = Pioneer2RS232InterfaceControl("COM3", 9600)
    # pioneer2 = ERS('COM6', 9600)
    # pioneer2.run()