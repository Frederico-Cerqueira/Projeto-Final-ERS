from navigation.sip_information.sonars import detect_obj, \
    detects_an_object_ahead, Direction, detects_an_object_left, detects_an_object_right
from navigation.command import Command
from navigation.utils import process_command, detect_trash, detect_limit, last_command_terminated, process_sip, \
    collect_trash
from datetime import datetime
from enum import Enum
from navigation.limit import Limit


# E1 - pulse e tempo
def initial_state(state_machine, ers, sip):
    """State E1: Sends the first command."""
    ers.command = Command('MOVE', 500)
    process_command(ers)
    state_machine.state = States.E2


def analyse_sip(state_machine, ers, sip):
    """State E2: Process SIP and check if it is an object, trash, limit or next command."""
    process_sip(ers, sip)
    if detect_obj(sip.sonars):
        state_machine.state = States.E3
        pass
    if detect_trash():
        state_machine.state = States.E4
        pass
    limit, status = detect_limit(sip.coordinates.x, state_machine.limit.x, sip.coordinates.y, state_machine.limit.y, state_machine)
    if limit:
        if status == "continue":
            state_machine.state = States.E5
            pass
        else:
            print("FIM DE AREA")
            ers.command = Command('STOP', None)
            process_command(ers)
            pass
    if last_command_terminated(ers, sip):
        state_machine.state = States.E6
        pass


def lim_or_obj(state_machine, ers, sip):
    """State E3: Check if there is an object or a limit."""
    process_sip(ers, sip)
    if detect_limit(sip.coordinates.x, state_machine.limit.x, sip.coordinates.y, state_machine.limit.y, state_machine):
        state_machine.state = States.E5
    else:
        ers.command = Command('STOP', None)
        process_command(ers)
        state_machine.y = sip.coordinates.y
        state_machine.wait_for_obj = datetime.now().timestamp()
        state_machine.state = States.E3a


def wait_for_obj(state_machine, ers):
    """State E3a: Waits for the object to move out of the way."""
    current_time = datetime.now().timestamp()
    if current_time - state_machine.wait_for_obj >= 5:
        state_machine.state = States.E3b


# E3b - vê se ainda há obj e vira para o lado certo e faz andar
def theres_still_obj(state_machine, ers, sip):
    """State E3b: Check if there is still an object and turn to the right or left direction depending
    on the information received from the sonars.
    """
    process_sip(ers, sip)
    direction = detects_an_object_ahead(sip.sonars)
    if direction != Direction.STAY:
        state_machine.dodge_direction = direction
        if direction is Direction.RIGHT:
            ers.command = Command('DHEAD', -90)  # DIREITA
            process_command(ers)
            ers.command = Command('MOVE', 10000)
            process_command(ers)
            state_machine.state = States.E3b2
        else:
            ers.command = Command('DHEAD', 90)  # ESQUERDA
            process_command(ers)
            ers.command = Command('MOVE', 10000)
            process_command(ers)
            state_machine.state = States.E3b1
        pass
    else:
        ers.command = Command('MOVE', 10000)
        process_command(ers)
        state_machine.state = States.E6
        pass


def wait_for_first_turn_left(state_machine, ers, sip):
    """State E3b1: Waits for the robot to finish turning left."""
    process_sip(ers, sip)
    if detects_an_object_right(sip.sonars) and not detect_obj(sip.sonars):
        state_machine.state = States.E3c
        pass


def wait_for_first_turn_right(state_machine, ers, sip):
    """State E3b2: Waits for the robot to finish turning right."""
    process_sip(ers, sip)
    if detects_an_object_left(sip.sonars) and not detect_obj(sip.sonars):
        state_machine.state = States.E3c
        pass


def first_move_while_obj(state_machine, ers, sip):
    """State E3c: Move until there is no object on the robot's side and turn."""
    process_sip(ers, sip)
    if state_machine.dodge_direction is Direction.RIGHT:
        if not detects_an_object_left(sip.sonars):
            ers.command = Command('DHEAD', 90)  # ESQUERDA
            process_command(ers)
            state_machine.state = States.E3c3
    else:
        if not detects_an_object_right(sip.sonars):
            ers.command = Command('DHEAD', -90)  # DIREITA
            process_command(ers)
            state_machine.state = States.E3c1


def wait_for_secondo_turn_right_while_obj(state_machine, ers, sip):
    """State E3c1: Waits for the robot to finish turning right while there is an object on its side."""
    process_sip(ers, sip)
    if detects_an_object_right(sip.sonars):
        state_machine.state = States.E3c2


def wait_for_second_turn_right_without_obj(state_machine, ers, sip):
    """State E3c2: Waits for the robot to finish turning right while there is no object on its side."""
    process_sip(ers, sip)
    if not detects_an_object_right(sip.sonars):
        state_machine.state = States.E3d


def wait_for_secondo_turn_left_while_obj(state_machine, ers, sip):
    """State E3c3: Waits for the robot to finish turning left while there is an object on its side."""
    process_sip(ers, sip)
    if detects_an_object_left(sip.sonars):
        state_machine.state = States.E3c4


def wait_for_second_turn_left_without_obj(state_machine, ers, sip):
    """State E3c4: Waits for the robot to finish turning left while there is no object on its side."""
    process_sip(ers, sip)
    if not detects_an_object_left(sip.sonars):
        state_machine.state = States.E3d


def move_until_obj(state_machine, ers, sip):
    """State E3d: Move until there is an object on the robot's side."""
    process_sip(ers, sip)
    if state_machine.dodge_direction is Direction.RIGHT:
        if detects_an_object_left(sip.sonars):
            state_machine.state = States.E3e
    else:
        if detects_an_object_right(sip.sonars):
            state_machine.state = States.E3e


# E3e - Fica a mover até deixar de ter obj na sua lateral e vira
def second_move_while_obj(state_machine, ers, sip):
    """State E3e: Move until there is no object on the robot's side and turn."""
    process_sip(ers, sip)
    if state_machine.dodge_direction is Direction.RIGHT:
        if not detects_an_object_left(sip.sonars):
            ers.command = Command('DHEAD', 90)  # ESQUERDA
            process_command(ers)
            state_machine.state = States.E3f
    else:
        if not detects_an_object_right(sip.sonars):
            ers.command = Command('DHEAD', -90)  # DIREITA
            process_command(ers)
            state_machine.state = States.E3f


def return_to_path(state_machine, ers, sip):
    """State E3f: Move until the robot is back on the path it was before the object appeared."""
    process_sip(ers, sip)
    if state_machine.dodge_direction is Direction.RIGHT:
        if sip.coordinates.y <= state_machine.y:
            ers.command = Command('DHEAD', -90)  # DIREITA
            process_command(ers)
            state_machine.state = States.E2
    else:
        if sip.coordinates.y >= state_machine.y:
            ers.command = Command('DHEAD', 90)  # ESQUERDA
            process_command(ers)
            state_machine.state = States.E2


def get_trash(state_machine, ers, sip):
    """State E4: Rotates when detects an obstacle."""
    ers.command = Command('STOP', None)
    sip.sonars[3].display_info()
    sip.sonars[4].display_info()
    process_command(ers)
    if detect_obj(sip.sonars):
        state_machine.state = States.E3
        pass
    state_machine.wait_for_turn = datetime.now().timestamp()
    ers.command = Command('DHEAD', 360)
    process_command(ers)
    state_machine.state = States.E4a


def wait_for_turn(state_machine, ers, sip):
    """State E4a: Waits for the robot to finish rotating."""
    current_time = datetime.now().timestamp()
    if current_time - state_machine.wait_for_turn >= 3:
        collect_trash()
        state_machine.state = States.E6


def change_direction(state_machine, ers, sip):
    """State E5: Changes the robot's direction when it reaches a limit."""
    process_sip(ers, sip)
    state_machine.novo_y = sip.coordinates.y
    if state_machine.lim_direction == 'front':  # 'front':
        ers.command = Command('DHEAD', -90)
        process_command(ers)
    else:
        ers.command = Command('DHEAD', 90)
        process_command(ers)
    state_machine.state = States.E5a


def rotate(state_machine, ers, sip):
    """State E5a: Rotates the robot 90 degrees to the right or left."""
    process_sip(ers, sip)
    ers.command = Command('MOVE', 300)
    process_command(ers)
    # -1001 <= 0 - 1000 = -1001 <= -1000
    if abs(sip.coordinates.y) >= (abs(state_machine.novo_y) + 300):
        if state_machine.lim_direction == 'front':  # 'front':
            ers.command = Command('DHEAD', -90)
            process_command(ers)
        else:
            ers.command = Command('DHEAD', 90)
            process_command(ers)
        state_machine.state = States.E5b


def backwards(state_machine, ers, sip):
    """State E5b: Moves the robot backwards until it reaches the limit."""
    process_sip(ers, sip)
    ers.command = Command('MOVE', 5000)
    process_command(ers)
    margin = 100
    if state_machine.lim_direction == 'front':  # 'front':
        if abs(sip.coordinates.x) <= abs(state_machine.limit.x - margin):
            # passar a back
            state_machine.lim_direction = 'back'
            state_machine.state = States.E6
    else:
        if abs(sip.coordinates.x) >= margin:
            # passar a front
            state_machine.lim_direction = 'front'
            state_machine.state = States.E6


def send_next_command(state_machine, ers):
    """State E6: Sends the next command."""
    ers.command = Command('MOVE', 5000)
    process_command(ers)
    state_machine.state = States.E2


class States(Enum):
    """Enumeration of the states of the state machine."""
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
    E4a = wait_for_turn
    E5 = change_direction,
    E5a = rotate,
    E5b = backwards,
    E6 = send_next_command,


class StateMachine:
    def __init__(self, height=3000, width=5000):
        """Initializes the state machine and its variables."""
        self.state = States.E1
        self.side = None
        self.y = 0
        self.wait_for_obj = datetime.now().timestamp()
        self.wait_for_turn = datetime.now().timestamp()
        self.dodge_direction = None
        self.count = 0
        self.novo_y = 0
        self.limit = Limit(height, width)
        self.lim_direction = 'front'

    def state_machine(self, ers, sip):
        """State machine for the robot's navigation."""
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
            get_trash(self, ers, sip)
        elif self.state == States.E4a:
            wait_for_turn(self, ers, sip)
        elif self.state == States.E5:
            change_direction(self, ers, sip)
        elif self.state == States.E5a:
            rotate(self, ers, sip)
        elif self.state == States.E5b:
            backwards(self, ers, sip)
        elif self.state == States.E6:
            send_next_command(self, ers)
