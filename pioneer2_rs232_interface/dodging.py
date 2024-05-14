from datetime import datetime

from pioneer2_rs232_interface.command import Command
from pioneer2_rs232_interface.sonars import update_sonar_info, Direction, \
    detects_an_object_left, detects_an_object_right, detects_an_object_ahead
from pioneer2_rs232_interface.state_machine import States
from pioneer2_rs232_interface.utils import process_command


# E4a
def dodge_obstacle(ers, sonars, state_machine):
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
def get_sip_for_dodge(state_machine, ers):
    initial = state_machine.initial_sip_time
    current = datetime.now().timestamp()
    if ers.__serial_communication.check_sip_availability() and (initial - current > 0.100):
        state_machine.initial_sip_time = datetime.now().timestamp()
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
