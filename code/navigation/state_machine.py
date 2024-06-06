from sip_information.sonars import detect_obj, \
    detects_an_object_ahead, Direction, detects_an_object_left, detects_an_object_right, print_sonar_info
from command import Command
from utils import process_command, detect_trash, detect_limit, last_command_terminated, process_sip
from datetime import datetime
from enum import Enum
from limit import Limit


# E1 - pulse e tempo
def initial_state(state_machine, ers, sip):
    ers.command = Command('MOVE', 5000)
    process_command(ers)
    state_machine.state = States.E2


# E2 - processa sip e vê se é obj, lixo, limite ou next cmd
def analyse_sip(state_machine, ers, sip):
    process_sip(ers, sip)
    if detect_obj(sip.sonars):
        print("obj detected")
        state_machine.state = States.E3
        pass
    if detect_trash():
        print("trash detected")
        state_machine.state = States.E4
        pass
    if detect_limit(sip.coordinates.x, state_machine.limit.x, sip.coordinates.y, state_machine.limit.y, state_machine):
        print("limit detected")
        state_machine.state = States.E5
        pass
    if last_command_terminated(ers, sip):
        print("last command terminated")
        state_machine.state = States.E6
        pass


# E3 - ve se é lim ou obj
def lim_or_obj(state_machine, ers, sip):
    process_sip(ers, sip)
    if detect_limit(sip.coordinates.x, state_machine.limit.x, sip.coordinates.y, state_machine.limit.y, state_machine):
        state_machine.state = States.E5
    else:
        print("ESTADO 3 OBJ")
        ers.command = Command('STOP', None)
        process_command(ers)
        state_machine.y = sip.coordinates.y
        print("y guardado: ", state_machine.y)
        state_machine.wait_for_obj = datetime.now().timestamp()
        state_machine.state = States.E3a


# E3a - espera pelo obj
def wait_for_obj(state_machine, ers):
    current_time = datetime.now().timestamp()
    if current_time - state_machine.wait_for_obj >= 5:
        print("continuar a andar")
        # ers.command = Command('MOVE', -250) ##VER
        # process_command(ers)
        state_machine.state = States.E3b


# E3b - vê se ainda há obj e vira para o lado certo e faz andar
def theres_still_obj(state_machine, ers, sip):
    process_sip(ers, sip)
    direction, S0, S7 = detects_an_object_ahead(sip.sonars)
    if direction != Direction.STAY:
        print("obj detected -  turn to ", direction)
        state_machine.dodge_direction = direction
        if direction is Direction.RIGHT:
            print("turn right - SO", S0, "S7", S7)
            ers.command = Command('DHEAD', -90)  # DIREITA
            process_command(ers)
            ers.command = Command('MOVE', 10000)
            process_command(ers)
            print("vou para o E3B2")
            state_machine.state = States.E3b2
        else:
            print("turn left - SO", S0, "S7", S7)
            ers.command = Command('DHEAD', 90)  # ESQUERDA
            process_command(ers)
            ers.command = Command('MOVE', 10000)
            process_command(ers)
            print("vou para o E3B1")
            state_machine.state = States.E3b1
        pass
    else:
        ers.command = Command('MOVE', 10000)
        process_command(ers)
        state_machine.state = States.E6
        pass


# E3b1 - Verifica se já virou à esquerda
def wait_for_first_turn_left(state_machine, ers, sip):
    process_sip(ers, sip)
    if detects_an_object_right(sip.sonars) and not detect_obj(sip.sonars):
        print("já virou a 1ª esquerda")
        print("VOU PARA O E3C")
        state_machine.state = States.E3c
        pass


# E3b2 - Verifica se já virou à direita
def wait_for_first_turn_right(state_machine, ers, sip):
    process_sip(ers, sip)
    if detects_an_object_left(sip.sonars) and not detect_obj(sip.sonars):
        print("já virou a 1ª direita")
        print("VOU PARA O E3C")
        state_machine.state = States.E3c
        pass


# E3c - Fica a mover até deixar de ter obj na sua lateral e vira
def first_move_while_obj(state_machine, ers, sip):
    process_sip(ers, sip)
    if state_machine.dodge_direction is Direction.RIGHT:
        sip.sonars[0].display_info()
        if not detects_an_object_left(sip.sonars):
            print("E3C - já não tem o objeto left")
            ers.command = Command('DHEAD', 90)  # ESQUERDA
            process_command(ers)
            print("Vou para o E3c3")
            state_machine.state = States.E3c3
    else:
        if not detects_an_object_right(sip.sonars):
            print("E3C - já não tem o objeto à direita")
            ers.command = Command('DHEAD', -90)  # DIREITA
            process_command(ers)
            print("Vou para o E3c1")
            state_machine.state = States.E3c1


# E3c1 - esperar enquanto vira à direita enquanto tem obj
def wait_for_secondo_turn_right_while_obj(state_machine, ers, sip):
    process_sip(ers, sip)
    if detects_an_object_right(sip.sonars):
        print("estou a virar")
        print("VOU PARA O E3C2")
        state_machine.state = States.E3c2


# E3C2 - espera até voltar a não ter obj enquanto vira à direita
def wait_for_second_turn_right_without_obj(state_machine, ers, sip):
    print("E3C2")
    process_sip(ers, sip)
    if not detects_an_object_right(sip.sonars):
        print("já virou")
        print("VOU PARA O E3D")
        state_machine.state = States.E3d


# E3c3 - esperar enquanto vira à esquerda enquanto tem obj
def wait_for_secondo_turn_left_while_obj(state_machine, ers, sip):
    process_sip(ers, sip)
    if detects_an_object_left(sip.sonars):
        print("estou a virar à esqueda")
        print("VOU PARA O E3C4")
        state_machine.state = States.E3c4


# E3C4 - espera até voltar a não ter obj enquanto vira à esquerda
def wait_for_second_turn_left_without_obj(state_machine, ers, sip):
    print("E3C4")
    process_sip(ers, sip)
    if not detects_an_object_left(sip.sonars):
        print("já virou À esquerda")
        print("VOU PARA O E3D")
        state_machine.state = States.E3d


# E3d - Fica a mover até ter obj na sua lateral
def move_until_obj(state_machine, ers, sip):
    print("E3D")
    process_sip(ers, sip)
    if state_machine.dodge_direction is Direction.RIGHT:
        if detects_an_object_left(sip.sonars):
            print(" E3D - TEM OBJETO LEFT")
            state_machine.state = States.E3e
    else:
        if detects_an_object_right(sip.sonars):
            print(" E3D - TEM OBJETO RIGHT")
            state_machine.state = States.E3e


# E3e - Fica a mover até deixar de ter obj na sua lateral e vira
def second_move_while_obj(state_machine, ers, sip):
    process_sip(ers, sip)
    if state_machine.dodge_direction is Direction.RIGHT:
        if not detects_an_object_left(sip.sonars):
            print("E3E - NÃO TEM OBJETO ESQ")
            ers.command = Command('DHEAD', 90)  # ESQUERDA
            process_command(ers)
            state_machine.state = States.E5
    else:
        if not detects_an_object_right(sip.sonars):
            print("E3E - NÃO TEM OBJETO DIR")
            ers.command = Command('DHEAD', -90)  # DIREITA
            process_command(ers)
            state_machine.state = States.E3f


# E3f - Anda até voltar ao caminho que estava antes do obj
def return_to_path(state_machine, ers, sip):
    process_sip(ers, sip)
    print("y atual: ", sip.coordinates.y)
    if state_machine.dodge_direction is Direction.RIGHT:
        if sip.coordinates.y <= state_machine.y:
            print("voltei ao sitio")
            ers.command = Command('DHEAD', -90)  # DIREITA
            process_command(ers)
            state_machine.state = States.E2
    else:
        if sip.coordinates.y >= state_machine.y:
            ers.command = Command('DHEAD', 90)  # ESQUERDA
            process_command(ers)
            state_machine.state = States.E2


# E4
def get_trash(state_machine, ers):
    ers.command = Command('STOP', None)
    process_command(ers)
    ers.command = Command('DHEAD', 360)
    process_command(ers)
    state_machine.state = States.E2


# E5
def change_direction(state_machine, ers, sip):
    process_sip(ers, sip)
    print("CHANGE DIRECTION")
    state_machine.novo_y = sip.coordinates.y
    if state_machine.sentido == 'back':  # 'front':
        ers.command = Command('DHEAD', -90)
        process_command(ers)
    else:
        ers.command = Command('DHEAD', 90)
        process_command(ers)
    state_machine.state = States.E5a


# E5a
def rodar(state_machine, ers, sip):
    process_sip(ers, sip)
    print("ENTROU NO RODAR")
    ers.command = Command('MOVE', 300)
    process_command(ers)

    # -1001 <= 0 - 1000 = -1001 <= -1000
    if abs(sip.coordinates.y) >= (abs(state_machine.novo_y) + 300):
        if state_machine.sentido == 'back':  # 'front':
            ers.command = Command('DHEAD', -90)
            process_command(ers)
        else:
            ers.command = Command('DHEAD', 90)
            process_command(ers)
        state_machine.state = States.E5b


# E5b
def backwards(state_machine, ers, sip):
    process_sip(ers, sip)
    print("ENTROU NO BACKWARDS")
    ers.command = Command('MOVE', 5000)
    process_command(ers)
    margin = 100
    if state_machine.sentido == 'back':  # 'front':
        if abs(sip.coordinates.x) <= abs(state_machine.limit.x - margin):
            state_machine.state = States.E6
    else:
        if abs(sip.coordinates.x) >= margin:
            state_machine.state = States.E6


# E6
def send_next_command(state_machine, ers):
    ers.command = Command('MOVE', 5000)
    process_command(ers)
    state_machine.state = States.E2


class States(Enum):
    E1 = initial_state,
    E2 = analyse_sip,
    E3 = lim_or_obj,
    E3a = wait_for_obj,
    E3b = theres_still_obj,
    E3b1 = wait_for_first_turn_left,
    E3b2 = wait_for_first_turn_right,
    E3c = first_move_while_obj,
    E3c1 = wait_for_secondo_turn_right_while_obj,
    E3c2 = wait_for_second_turn_right_without_obj,
    E3c3 = wait_for_secondo_turn_left_while_obj,
    E3c4 = wait_for_second_turn_left_without_obj
    E3d = move_until_obj,
    E3e = second_move_while_obj,
    E3f = return_to_path,
    E4 = get_trash,
    E5 = change_direction,
    E5a = rodar,
    E5b = backwards,
    E6 = send_next_command


class StateMachine:
    def __init__(self):
        self.state = States.E1
        self.side = None
        # MUDAR O NOME
        self.y = 0
        self.wait_for_obj = datetime.now().timestamp()
        self.dodge_direction = None
        self.count = 0
        self.novo_y = 0
        self.limit = Limit(3000, 3000)
        self.sentido = 'front'

    def state_machine(self, ers, sip):
        if self.state == States.E1:
            initial_state(self, ers, sip)
        elif self.state == States.E2:
            analyse_sip(self, ers, sip)
        elif self.state == States.E3:
            lim_or_obj(self, ers, sip)
        elif self.state == States.E3b1:
            wait_for_first_turn_left(self, ers, sip)
        elif self.state == States.E3b2:
            wait_for_first_turn_right(self, ers, sip)
        elif self.state == States.E3a:
            wait_for_obj(self, ers)
        elif self.state == States.E3b:
            theres_still_obj(self, ers, sip)
        elif self.state == States.E3c:
            first_move_while_obj(self, ers, sip)
        elif self.state == States.E3c1:
            wait_for_secondo_turn_right_while_obj(self, ers, sip)
        elif self.state == States.E3c2:
            wait_for_second_turn_right_without_obj(self, ers, sip)
        elif self.state == States.E3c3:
            wait_for_secondo_turn_left_while_obj(self, ers, sip)
        elif self.state == States.E3c4:
            wait_for_second_turn_left_without_obj(self, ers, sip)
        elif self.state == States.E3d:
            move_until_obj(self, ers, sip)
        elif self.state == States.E3e:
            second_move_while_obj(self, ers, sip)
        elif self.state == States.E3f:
            return_to_path(self, ers, sip)
        elif self.state == States.E4:
            get_trash(self, ers)
        elif self.state == States.E5:
            change_direction(self, ers, sip)
        elif self.state == States.E6:
            send_next_command(self, ers)
