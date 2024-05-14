from pioneer2_rs232_interface.main_ers import ERS

if __name__ == '__main__':
    pioneer2 = ERS('COM3', 9600)
    try:
        pioneer2.run()
        pioneer2.turn_off()
    except BaseException as e:
        print("Error during execution:", e)
    finally:
        pioneer2.turn_off()