from navigation.ers import ERS
from navigation.state_machine import StateMachine

if __name__ == '__main__':
    pioneer2 = ERS('COM10', 9600)
    machine = StateMachine()
    try:
        pioneer2.run(machine)
    except BaseException as e:
        print("Error during execution:", e)
    finally:
        pioneer2.turn_off()
