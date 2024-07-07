from dataclasses import dataclass
from enum import Enum
import threading
import time
from flask import Flask, jsonify, request

from navigation.ers import ERS
from navigation.state_machine import StateMachine

app = Flask(__name__)

@dataclass
class TaskStartOutputModel:
    id: int
    status: str
    name: str
    areaId: int
    height: int
    width: int
    timeId: int
    weekDay: str
    startTime: str
    endTime: str


class API:
    def __init__(self, pioneer2):
        self.task_data = None
        self.running = False
        self.pioneer2 = pioneer2
        app.add_url_rule("/start", view_func=self.start_task, methods=['POST'])
        app.add_url_rule("/stop/<int:id>", view_func=self.stop_task, methods=['GET'])

    def run_ers(self):
        self.running = True
        machine = StateMachine(self.task_data.height, self.task_data.width)
        self.pioneer2.run(machine)
        self.running = False

    def start_task(self):
        if self.running:
            return jsonify({'message': 'ERS is already running'}), 400

        data = request.json
        task_data = TaskStartOutputModel(**data)
        print("Area", task_data.height,task_data.width)
        self.task_data = task_data

        # Inicia o ERS em um thread separado
        threading.Thread(target=self.run_ers).start()

        return jsonify({'message': 'Task started successfully'})

    def stop_task(self, id):
        self.pioneer2.stop()
        return jsonify({'message': 'Task stopped successfully'})

if __name__ == "__main__":
    pioneer2 = ERS('COM6', 9600)
    API(pioneer2)
    try:
        app.run()
    except BaseException as e:
        print("Error during execution:", e)
    finally:
        pioneer2.turn_off()
