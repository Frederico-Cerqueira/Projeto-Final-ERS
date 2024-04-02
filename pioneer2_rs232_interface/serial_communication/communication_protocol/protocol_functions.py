# -*- coding: utf-8 -*-

"""

"""


def calculate_int_data_bytes(value):
    argument_type = b'\x3b'  # Inteiro positivo

    # Inteiro negativo
    if value < 0:
        value = abs(value)  # Inteiros negativos são transmitidos como o seu valor absoluto
        argument_type = b'\x1b'

    return argument_type + value.to_bytes(2, byteorder='little', signed=True)


def calculate_packet_checksum(packet):
    packet = packet[2:]

    n_bites = packet[0] - 2  # Vai buscar o primeiro byte que irá dar o tamanho do packet e é retirado os bytes de checksum
    index = 0  # É necessário para percorrer o array de bytes
    packet_no_checksum = packet[1:-2]  # retira o checksum inicial que ira ser igual a zero
    checksum = 0  # inicializa o checksum a zero

    while (index < n_bites - 1):
        checksum += packet_no_checksum[index] << 8 | packet_no_checksum[index + 1]  # vai buscar ao array uma word
        checksum = checksum & int.from_bytes(b'\xff\xff', byteorder="little")  # faz a operação & com o checksum anterior
        index += 2  # aumenta o indice do array para ir buscar a próxima word

    if (n_bites > index):  # caso seja impar é feita a operação XOR com o último byte do array
        checksum = checksum ^ packet_no_checksum[n_bites - 1]

    checksum = checksum.to_bytes(2, byteorder='big')

    return checksum


def validate_checksum(packet):
    # packet = b'\xfa\xfb\x90\x01\x02\x03\x04\xfe\xff'

    packet_data = packet[2:-2]
    packet_checksum = packet[-2:]

    validation = packet_data ^ packet_checksum

    if validation == b'\0x00':
        return True
    else:
        return False
