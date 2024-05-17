from datetime import datetime

from command import Command
from sonars import detects_an_object_ahead, Direction, update_sonar_info, detects_an_object_left, \
    detects_an_object_right
from state_machine import States
from utils import process_command



