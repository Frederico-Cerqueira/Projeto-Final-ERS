from pioneer2_rs232_interface.sip_information.coordinates import update_coordinate_info
from pioneer2_rs232_interface.sip_information.sonars import detect_obj, update_sonar_info, print_sonar_info
from command import Command
from utils import process_command, detect_trash, detect_limit, last_command_terminated, process_sip
from datetime import datetime
from enum import Enum


# E1 - pulse e tempo
def initial_state(state_machine, ers):
    print("ENTROU no E1")
    ers.command = Command('MOVE', 5000)
    process_command(ers)
    state_machine.state = States.E2


# E2 - processa sip e vê se é obj, lixo, limite ou next cmd
def process_sip1(state_machine, ers, sip):
    process_sip(ers, sip)
    print_sonar_info(sip.sonars)
    if detect_obj(sip.sonars):
        print("obj detected")
        state_machine.state = States.E3
    if detect_trash():
        print("trash detected")
        state_machine.state = States.E4
    if detect_limit():
        print("limit detected")
        state_machine.state = States.E5
    if last_command_terminated(ers, sip):
        print("last command terminated")
        state_machine.state = States.E6


# E3 - ve se é lim ou obj
def lim_or_obj(ers, state_machine, sip):
    process_sip(ers, sip)
    if detect_limit():
        state_machine.state = States.E5
    else:
        ers.command = Command('STOP', None)
        process_command(ers)
        state_machine.wait_for_obj = datetime.now().timestamp()
        state_machine.state = States.E3a


# E3a - espera pelo obj
def wait_for_obj(state_machine, ers):
    current_time = datetime.now().timestamp()
    if current_time - state_machine.wait_for_obj >= 2:
        ers.command = Command('MOVE', -200)
        process_command(ers)
        state_machine.state = States.E3b


# E3b - vê se ainda há obj e vira para o lado certo e faz andar
def theres_still_obj(state_machine, ers, sip):
    process_sip(ers, sip)
    direction = sip.sonars.detects_an_object_ahead(sip.sonars)
    if direction != sip.sonars.Direction.STAY:
        print("obj detected -  turn to ", direction)
        state_machine.dodge_direction(direction)
        state_machine.x = sip.coordinates.x
        if state_machine.dodge_direction is sip.sonars.Direction.RIGHT:
            ers.command = Command('HEAD', -90)  # DIREITA
            process_command(ers)
            ers.command = Command('MOVE', 5000)
            process_command(ers)
        else:
            ers.command = Command('HEAD', 90)  # ESQUERDA
            process_command(ers)
            ers.command = Command('MOVE', 5000)
            process_command(ers)
        state_machine.state = States.E3c
    else:
        ers.command = Command('HEAD', 180)
        state_machine.state = States.E6


# E3c - Fica a mover até deixar de ter obj na sua lateral e vira
def first_move_until_obj(state_machine, ers, sip):
    process_sip(ers, sip)
    if state_machine.dodge_direction is sip.sonars.Direction.RIGHT:
        if sip.sonars.detects_an_object_left(sip.sonars):
            state_machine.dist = sip.coordinates.x - state_machine.x
            ers.command = Command('HEAD', 90)  # ESQUERDA
            process_command(ers)
            state_machine.state = States.E3d
    else:
        if sip.sonars.detects_an_object_right(sip.sonars):
            ers.command = Command('HEAD', -90)  # DIREITA
            process_command(ers)
            state_machine.state = States.E3d


# E3d - Fica a mover até ter obj na sua lateral
def move_while_obj(state_machine, ers, sip):
    process_sip(ers, sip)
    if state_machine.dodge_direction is sip.sonars.Direction.RIGHT:
        if sip.sonars.detects_an_object_left(sip.sonars):
            state_machine.state = States.E3e
    else:
        if sip.sonars.detects_an_object_right(sip.sonars):
            state_machine.state = States.E3e


# E3e - Fica a mover até deixar de ter obj na sua lateral e vira
def second_move_until_obj(state_machine, ers, sip):
    process_sip(ers, sip)
    if state_machine.dodge_direction is sip.sonars.Direction.RIGHT:
        if sip.sonars.detects_an_object_left(sip.sonars):
            state_machine.x = sip.coordinates.x
            ers.command = Command('HEAD', 90)  # ESQUERDA
            process_command(ers)
            state_machine.state = States.E3f
    else:
        if sip.sonars.detects_an_object_right(sip.sonars):
            state_machine.x = sip.coordinates.x
            ers.command = Command('HEAD', -90)  # DIREITA
            process_command(ers)
            state_machine.state = States.E3f


# E3f - Anda até voltar ao caminho que estava antes do obj
def return_to_path(state_machine, ers, sip):
    process_sip(ers, sip)
    if sip.coordinates.x - state_machine.dist == state_machine.x:
        if state_machine.dodge_direction is sip.sonars.Direction.RIGHT:
            ers.command = Command('HEAD', -90)  # DIREITA
            process_command(ers)
            state_machine.state = States.E2
        else:
            ers.command = Command('HEAD', 90)  # ESQUERDA
            process_command(ers)
            state_machine.state = States.E2


# E4
def get_trash(state_machine, ers):
    ers.command = Command('STOP', None)
    process_command(ers)
    ers.command = Command('HEAD', 360)
    process_command(ers)
    state_machine.state = States.E2


def change_direction(state_machine, ers):
    pass


"""
# E5
def change_direction(state_machine, ers, limit, initial_side):
    x_pos = ers.sip_info['x_pos']
    y_pos = ers.sip_info['y_pos']
    x_limit, y_limit = limit
    state_machine.side = initial_side
    if detect_limit(x_pos, x_limit, y_pos, y_limit):
        if state_machine.side == 'left':
            state_machine.side = 'right'
            ers.command = Command('HEAD', -180)
            ers.command = Command('MOVE', 1000)
        else:
            state_machine.side = 'left'
            ers.command = Command('HEAD', 180)
            ers.command = Command('MOVE', 1000)
    state_machine.state = States.E6
"""
"""
    if detect_limit(x_pos, x_limit, y_pos, y_limit):
        if state_machine.side == 'left':
            state_machine.side = 'right'
            ers.command = Command('HEAD', -90)
            process_command(ers)
            if last_command_terminated(ers):
                ers.command = Command('MOVE', 1000)
                process_command(ers)
                if last_command_terminated(ers):
                    ers.command = Command('HEAD', -90)
                    process_command(ers)
                    state_machine.state = States.E2
        else:
            state_machine.side = 'left'
            ers.command = Command('HEAD', 90)
            process_command(ers)
            if last_command_terminated(ers):
                ers.command = Command('MOVE', 1000)
                process_command(ers)
                if last_command_terminated(ers):
                    ers.command = Command('HEAD', 90)
                    process_command(ers)
                    state_machine.state = States.E2"""


# E6
def send_next_command(state_machine, ers):
    ers.command = Command('MOVE', 5000)
    process_command(ers)
    state_machine.state = States.E2



class States(Enum):
    E1 = initial_state,
    E2 = process_sip1,
    E3 = lim_or_obj,
    E3a = wait_for_obj,
    E3b = theres_still_obj,
    E3c = first_move_until_obj,
    E3d = move_while_obj,
    E3e = second_move_until_obj,
    E3f = return_to_path,
    E4 = get_trash,
    E5 = change_direction,
    E6 = send_next_command,


class StateMachine:
    def __init__(self):
        self.state = States.E1
        self.side = None
        self.x = 0
        self.y = 0
        self.dist = 0
        self.wait_for_obj = datetime.now().timestamp()
        self.dodge_direction = None

    def state_machine(self, ers, sip):
        print(self.state.name)
        if self.state == States.E1:
            initial_state(self, ers)
        elif self.state == States.E2:
            process_sip1(self, ers, sip)
        elif self.state == States.E3:
            lim_or_obj(self, ers, sip)
        elif self.state == States.E3a:
            wait_for_obj(self, ers)
        elif self.state == States.E3b:
            theres_still_obj(self, ers, sip)
        elif self.state == States.E3c:
            first_move_until_obj(self, ers, sip)
        elif self.state == States.E3d:
            move_while_obj(self, ers, sip)
        elif self.state == States.E3e:
            second_move_until_obj(self, ers, sip)
        elif self.state == States.E3f:
            return_to_path(self, ers, sip)
        elif self.state == States.E4:
            get_trash(self, ers)
        elif self.state == States.E5:
            change_direction(self, ers)
        elif self.state == States.E6:
            send_next_command(self, ers)
