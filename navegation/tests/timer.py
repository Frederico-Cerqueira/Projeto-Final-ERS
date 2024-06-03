from datetime import datetime


class Timer:

    def __init__(self, tempo_espera=None):
        self._tempo_inicial = 0
        self._tempo_final = 0
        self._tempo_espera = tempo_espera
        self._is_waiting = True
        self._is_counting = False

    def start(self):
        self.reset()
        self.set_waiting(False)
        self.set_counting(True)

        self._tempo_inicial = datetime.now().timestamp()

    def stop(self):
        self.set_counting(False)

        self._tempo_final = datetime.now().timestamp()

        return self.get_final_time()

    def reset(self):
        self._tempo_inicial = 0
        self._tempo_final = 0
        self.set_waiting(True)
        self.set_counting(False)

    def set_waiting(self, state):
        self._is_waiting = state

    def set_counting(self, state):
        self._is_counting = state

    def get_final_time(self):
        return self._tempo_final - self._tempo_inicial

    def get_is_waiting(self):
        return self._is_waiting

    def get_is_counting(self):
        return self._is_counting
