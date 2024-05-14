from pioneer2_rs232_interface.ers_final import ERS
from pioneer2_rs232_interface.state_machine import StateMachine

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
