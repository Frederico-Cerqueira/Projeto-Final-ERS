from ers import ERS
from state_machine import StateMachine

if __name__ == '__main__':
    pioneer2 = ERS('COM3', 9600)
    machine = StateMachine()
    try:
        pioneer2.run(machine)
        pioneer2.turn_off()
    except BaseException as e:
        print("Error during execution:", e)
    finally:
        pioneer2.turn_off()
