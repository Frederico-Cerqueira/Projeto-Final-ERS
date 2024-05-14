from datetime import datetime
from enum import Enum

from pioneer2_rs232_interface.command import Command
from pioneer2_rs232_interface.dodging import get_sip_for_dodge, process_sip_for_dodge, dodge_obstacle_left, \
    dodge_obstacle_right, dodge_obstacle
from pioneer2_rs232_interface.sonars import update_sonar_info, detect_obj
from pioneer2_rs232_interface.utils import process_command, detect_trash, detect_limit, last_command_terminated


# E1
def initial_state(ers, state_machine):
    ers.command = Command('MOVE', 1000)
    process_command(ers)
    state_machine.state = States.E2


# E2
def get_sip(ers, state_machine):
    initial = ers.init_time_sip
    current = datetime.now().timestamp()
    if ers.__serial_communication.check_sip_availability() and (current - initial > 0.100):
        ers.init_time_sip = datetime.now().timestamp()
        sip_info_aux = ers.__serial_communication.get_sip()
        if sip_info_aux != ers.sip_info:
            ers.sip_info = sip_info_aux
            state_machine.state = States.E3


# E3
def process_sip(ers, state_machine, sonars):
    sip = ers.sip_info
    update_sonar_info(sip['sonars'], sonars)
    if detect_obj(sonars):
        print("obj detected")
        state_machine.wait_for_obs = datetime.now().timestamp()
        if not state_machine.time_obj_elapsed:
            ers.command = Command('STOP', None)
            process_command(ers)
        state_machine.state = States.E4
    if detect_trash():
        print("trash detected")
        state_machine.state = States.E5
    if detect_limit():
        print("limit detected")
        state_machine.state = States.E6
    if last_command_terminated(ers):
        print("last command terminated")
        state_machine.state = States.E7


# E4
def rate_obstacle(ers, state_machine):
    if state_machine.time_obj_elapsed is True:
        if detect_limit():
            state_machine.state = States.E6
            state_machine.time_obj_elapsed = False
        else:
            state_machine.state = States.E4a
            state_machine.time_obj_elapsed = False
    else:
        if datetime.now().timestamp() - state_machine.wait_for_obs >= 2:
            state_machine.time_obj_elapsed = True
            ers.command = Command('MOVE', 1000)
            process_command(ers)
            state_machine.state = States.E2


# E6
# TODO
def change_direction():
    # O robô chegou a um limite definido, deve virar-se e voltar par
    pass


def get_trash(ers, state_machine):
    ers.command = Command('STOP', None)
    process_command(ers)
    ers.command = Command('HEAD', 360)
    process_command(ers)
    state_machine.state = States.E2


def send_next_command(ers, state_machine):
    ers.command = Command('MOVE', 1000)
    process_command(ers)
    state_machine.state = States.E2


class States(Enum):
    E1 = initial_state,
    E2 = get_sip,
    E3 = process_sip,
    E4 = rate_obstacle,
    E4a = dodge_obstacle,
    E4a1 = get_sip_for_dodge,
    E4a2 = process_sip_for_dodge,
    E4a3 = dodge_obstacle_left,
    E4a4 = dodge_obstacle_right,
    E5 = get_trash,
    E6 = change_direction,
    E7 = send_next_command,


class StateMachine:
    def __init__(self):
        self.state = States.E1
        # self.initial_pulse_time = datetime.now().timestamp()
        # self.initial_sip_time = datetime.now().timestamp()
        self.wait_for_obs = datetime.now().timestamp()
        self.time_obj_elapsed = False
        self.dodge_direction = None
        self.dodge_counter = 0

    def state_machine(self, ers, sonars):
        if self.state == States.E1:
            self.state.value(ers, self)
        elif self.state == States.E2:
            self.state.value(ers, self)
        elif self.state == States.E3:
            self.state.value(ers, self, sonars)
        elif self.state == States.E4:
            self.state.value(ers, self)
        elif self.state == States.E4a:
            self.state.value(ers, self, sonars)
        elif self.state == States.E4a1:
            self.state.value(ers, self)
        elif self.state == States.E4a2:
            self.state.value(ers, self, sonars)
        elif self.state == States.E4a3:
            self.state.value(ers, self)
        elif self.state == States.E4a4:
            self.state.value(ers, self)
        elif self.state == States.E5:
            self.state.value(ers, self)
        elif self.state == States.E6:
            self.state.value()
        elif self.state == States.E7:
            self.state.value(ers, self)
