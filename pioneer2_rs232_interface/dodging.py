from datetime import datetime

from command import Command
from sonars import detects_an_object_ahead, Direction, update_sonar_info, detects_an_object_left, \
    detects_an_object_right
from state_machine import States
from utils import process_command


# E4a
def dodge_obstacle(ers, state_machine, sonars):
    direction = detects_an_object_ahead(sonars)
    if direction == Direction.LEFT:
        state_machine.dodge_direction = direction
        ers.command = Command('HEAD', 90)
        process_command(ers)
        state_machine.state = States.E4a1
    elif direction == Direction.RIGHT:
        state_machine.dodge_direction = direction
        ers.command = Command('HEAD', -90)
        process_command(ers)
        state_machine.state = States.E4a1
    else:
        print("oopsie")


# E4a1
def get_sip_for_dodge(ers, state_machine):
    initial = ers.init_time_sip
    current = datetime.now().timestamp()
    if ers.__serial_communication.check_sip_availability() and (current - initial > 0.100):
        ers.init_time_sip = datetime.now().timestamp()
        sip_info_aux = ers.__serial_communication.get_sip()
        if sip_info_aux != ers.sip_info:
            ers.sip_info = sip_info_aux
            state_machine.state = States.E4a2


# E4a2
def process_sip_for_dodge(ers, state_machine, sonars):
    sip = ers.sip_info
    update_sonar_info(sip['sonars'], sonars)
    if state_machine.dodge_direction is Direction.RIGHT:
        # TEM UM OBS À ESQUERDA
        if detects_an_object_left(sonars) == Direction.LEFT:
            state_machine.state = States.E4a3
        else:
            state_machine.state = States.E4a1
    elif state_machine.dodge_direction is Direction.LEFT:
        if detects_an_object_right(sonars) == Direction.RIGHT:
            state_machine.state = States.E4a2
        else:
            state_machine.state = States.E4a1


# E4a3
def dodge_obstacle_left(ers, state_machine):
    if state_machine.dodger_counter == 3:
        ers.command = Command('HEAD', -90)
        process_command(ers)
        state_machine.state = States.E2
    ers.command = Command('HEAD', 90)
    process_command(ers)
    state_machine.state = States.E4a1


# E4a4
def dodge_obstacle_right(ers, state_machine):
    if state_machine.dodger_counter == 3:
        ers.command = Command('HEAD', 90)
        process_command(ers)
        state_machine.state = States.E2
    ers.command = Command('HEAD', -90)
    process_command(ers)
    state_machine.state = States.E4a1
