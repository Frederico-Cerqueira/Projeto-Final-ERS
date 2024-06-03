# -*- coding: utf-8 -*-

"""

"""


def test_movement_translation(distance, max_velocity, acceleration):
    commands = [
        'SETO',
        'SETV ' + str(max_velocity),  # Definir velocidade máxima
        'SETA ' + str(acceleration),  # Definir aceleração
        'SETA -' + str(acceleration),  # Definir desaceleração
        'MOVE ' + str(distance)  # Definir distância a percorrer
    ]

    return commands


def test_movement_rotation(degrees, max_velocity, acceleration):
    commands = [
        'SETO',
        'SETRV ' + str(max_velocity),  # Definir velocidade máxima da rotação
        'SETRA ' + str(acceleration),  # Definir aceleração da rotação
        'SETRA -' + str(acceleration),  # Definir desaceleração da rotação
        'DHEAD ' + str(degrees)  # Definir graus a rodar
    ]

    return commands


if __name__ == '__main__':
    value = 65535
    mask_ignore_first_bit = 0x7FFF  # Retirar o primeiro bit
    mask_ignore_last_bit = 0xFFFE  # Retirar o último bit
    result_ignore_first_bit = value & mask_ignore_first_bit
    result_ignore_last_bit = value & mask_ignore_last_bit

    print('Valor inteiro: ', value)
    print('Máscara que ignora o primeiro bit: ', mask_ignore_first_bit)
    print('Máscara que ignora o último bit: ', mask_ignore_last_bit)
    print('Resultado ao ignorar o primeiro bit: ', result_ignore_first_bit)
    print('Resultado ao ignorar o último bit: ', result_ignore_last_bit)
