# -*- coding: utf-8 -*-


"""
"""

# Contantes do Pioneer2 DX usadas para converter diversas unidades
DIST_CONV_FACTOR = 0.84  # Converter as coordenadas do codificador das rodas para milímetros
ANGLE_CONV_FACTOR = 0.087890625  # Converter a orientação do robô para graus
VEL_CONV_FACTOR = 1.0  # Converter a velocidade para milímetros / segundo
RANGE_CONV_FACTOR = 0.268  # Converter a leitura dos sonares para milímetros


def get_server_information_packet(packet):
    SIP_information = None

    # Verificar se o header está correcto
    if packet[:2] == b'\xfa\xfb':
        # Verificar tamanho do pacote
        packet_size = packet[2]

        # Verificar o tipo de pacote
        sip_type = __get_sip_type(packet[3])
        if sip_type == 'standard':
            SIP_information = __get_sip_data(packet[3:])

        # Verificar se o checksum está correcto
        # TODO
            
    return SIP_information


def __get_sip_type(packet_type):
    ptype = None
    # 0x32 = 50 e 0x33 = 51 
    if packet_type == 50 or packet_type == 51:
        ptype = 'standard'

    return ptype


def __get_sip_data(data):
    # Gerar dicionario que vai conter toda a informação vinda do SIP
    sip_data = {
        "motor_status":  True if data[0] == 51 else False,
        "x_pos": int.from_bytes(data[1:3], byteorder='little', signed=True) * DIST_CONV_FACTOR,
        "y_pos": int.from_bytes(data[3:5], byteorder='little', signed=True) * DIST_CONV_FACTOR,
        "th_pos": int.from_bytes(data[5:7], byteorder='little', signed=True) * ANGLE_CONV_FACTOR,
        "l_vel": int.from_bytes(data[7:9], byteorder='little', signed=True) * VEL_CONV_FACTOR,
        "r_vel": int.from_bytes(data[9:11], byteorder='little', signed=True) * VEL_CONV_FACTOR,
        "battery": data[11],
        "stall_and_bumpers": int.from_bytes(data[12:14], byteorder='little'),
        "control": int.from_bytes(data[14:16], byteorder='little', signed=True) * ANGLE_CONV_FACTOR,
        "flags": int.from_bytes(data[16:18], byteorder='little', signed=True),
        "compass": data[18],
        "sonar_count": data[19]
    }
    # Obter dados dos sonares
    byte_position = 20
    sonars_readings = []
    for i in range(sip_data["sonar_count"]):
        sonar_reading = {
            "sonar_number": data[byte_position],
            "sonar_range": int.from_bytes(data[byte_position+1:byte_position+3], byteorder='little') * RANGE_CONV_FACTOR
        }

        sonars_readings.append(sonar_reading)
        byte_position += 3

    sip_data["sonars"] = sonars_readings

    sip_data["grip_state"] = data[byte_position]
    sip_data["an_port"] = data[byte_position+1]
    sip_data["analog"] = data[byte_position+2]
    sip_data["dig_in"] = data[byte_position+3]
    sip_data["dig_out"] = data[byte_position+4]

    return sip_data


# TODO
def __get_config_sip_data(data):
    """"""
    # index = 0

    # self.__robot_type_size_str = data[index]
    # index += 1

    # self.__robot_type_str = data[index:index + self.__robot_type_size_str]        
    # index += self.__robot_type_size_str + 1

    # self.__robot_subtype_size_str =  data[index]
    # index += 1

    # self.__robot_subtype_str =  data[index:index + self.__robot_subtype_size_str]
    # index += self.__robot_subtype_size_str + 1

    # self.__robot_serial_numb_size_str = data[index]
    # index += 1

    # self.__robot_serial_numb_str = data[index:index + self.__robot_serial_numb_size_str]
    # index += self.__robot_serial_numb_size_str + 1

    # self.__4mots = data[index]
    # index += 1

    # self.__rot_vel_top = int.from_bytes(data[index:index+2], byteorder='big')
    # index += 3

    # self.__trans_vel_top = int.from_bytes(data[index:index+2], byteorder='big')
    # index += 3

    # self.__rot_acc_top = int.from_bytes(data[index:index+2], byteorder='big')
    # index += 3

    # self.__trans_acc_top = int.from_bytes(data[index:index+2], byteorder='big')
    # index += 3

    # self.__pwm_max = int.from_bytes(data[index:index+2], byteorder='big')
    # index += 3

    # self.__name_size_str = data[index]
    # index += 1

    # self.__name_size_str = data[index:index + self.__name_size_str]
    # index += self.__name_size_str + 1

    # self.__SIP = data[index]
    # index += 1

    # self.__host_baud = data[index]
    # index += 1

    # self.__aux_baud = data[index]
    # index += 1

    # self.__gripper = int.from_bytes(data[index:index+2], byteorder='big')
    # index += 3

    # self.__front_sonar = int.from_bytes(data[index:index+2], byteorder='big')
    # index += 3

    # self.__rear_sonar = int.from_bytes(data[index:index+2], byteorder='big')
    # index += 3

    # self.__rear_sonar = data[index]
    # index += 1

    # self.__low_battery = int.from_bytes(data[index:index+2], byteorder='big')
    # index += 3

    # self.__rev_count = int.from_bytes(data[index:index+2], byteorder='big')
    # index += 3

    # self.__watchdog = int.from_bytes(data[index:index+2], byteorder='big')
    # index += 3

    # self.__p2mpacs = data[index]
    # index += 1

    # self.__stall_val = int.from_bytes(data[index:index+2], byteorder='big')
    # index += 3

    # self.__stall_count = int.from_bytes(data[index:index+2], byteorder='big')
    # index += 3

    # self.__joy_vel = int.from_bytes(data[index:index+2], byteorder='big')
    # index += 3

    # self.__joy_R_vel = int.from_bytes(data[index:index+2], byteorder='big')
    # index += 3

    # self.__rot_vel_max = int.from_bytes(data[index:index+2], byteorder='big')
    # index += 3

    # self.__trans_vel_max = int.from_bytes(data[index:index+2], byteorder='big')
    # index += 3

    # self.__rot_acc = int.from_bytes(data[index:index+2], byteorder='big')
    # index += 3

    # self.__rot_decel = int.from_bytes(data[index:index+2], byteorder='big')
    # index += 3

    # self.__rot_kp = int.from_bytes(data[index:index+2], byteorder='big')
    # index += 3

    # self.__rot_kv = int.from_bytes(data[index:index+2], byteorder='big')
    # index += 3

    # self.__rot_ki = int.from_bytes(data[index:index+2], byteorder='big')
    # index += 3

    # self.__trans_acc = int.from_bytes(data[index:index+2], byteorder='big')
    # index += 3

    # self.__trans_decel = int.from_bytes(data[index:index+2], byteorder='big')
    # index += 3

    # self.__trans_kp = int.from_bytes(data[index:index+2], byteorder='big')
    # index += 3

    # self.__trans_kv = int.from_bytes(data[index:index+2], byteorder='big')
    # index += 3

    # self.__trans_ki = int.from_bytes(data[index:index+2], byteorder='big')
    # index += 3
    pass
