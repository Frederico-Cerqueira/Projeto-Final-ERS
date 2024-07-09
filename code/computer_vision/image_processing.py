import cv2
import numpy as np
import os
#import matplotlib.pyplot as plt
from computer_vision.utils import in_file
"""
# Outside Algorithm
def image_processing(img):
    # image = cv2.imread(img)

    image = cv2.resize(img, None, fx=0.25, fy=0.25)

    hsv_image = cv2.cvtColor(image, cv2.COLOR_BGR2HSV)

    h_value = most_present_h_value(image, hsv_image)

    matrix = (64 > hsv_image[:, :, 0]).astype(np.uint8)

    cv2.imshow('matrix', matrix * 255)

    cv2.waitKey(0)
    cv2.destroyAllWindows()


def most_present_h_value(img):
    # Read the input RGB image
    image = cv2.imread(img)

    # Resize the image to 25% of the original size
    image = cv2.resize(image, None, fx=0.25, fy=0.25)

    # Converter a imagem para HSV
    hsv_image = cv2.cvtColor(image, cv2.COLOR_BGR2HSV)
    # Extrair a componente H
    h_channel = hsv_image[:, :, 0]

    # Calcular o histograma da componente H
    hist_h = cv2.calcHist([h_channel], [0], None, [180], [0, 180])  # Usar 180 bins para H (0-179)

    # Encontrar o valor de H com a maior frequência
    max_h_index = np.argmax(hist_h)
    max_h_value = max_h_index

    print(f'O valor de H mais presente na imagem é: {max_h_value}')

    # Exibir a imagem original e a imagem em HSV
    cv2.imshow('Imagem Original', image)
    cv2.imshow('Imagem HSV', hsv_image)

    # Plotar o histograma da componente H
    plt.figure(figsize=(8, 6))
    plt.plot(hist_h, color='b')
    plt.xlim([0, 180])
    plt.title('Histograma da Componente H (Matiz)')
    plt.xlabel('Valor de H')
    plt.ylabel('Frequência')
    plt.grid(True)
    plt.show()

    return max_h_value


# Definir imagem_hsv como global
imagem_hsv = None


def obter_valor_hue(event, x, y, flags, param):
    global imagem_hsv
    if event == cv2.EVENT_LBUTTONDOWN:
        # Quando ocorre um clique com o botão esquerdo do mouse
        valor_hue = imagem_hsv[y, x, 0]
        print(f'Coordenadas do pixel ({x}, {y}) - Valor de H (Hue): {valor_hue}')


def main():
    global imagem_hsv

    image_directory = os.path.join('data', 'pi-images', 'outside')
    image_filename = 'area.jpg'
    image_path = os.path.join(image_directory, image_filename)

    # Read the input RGB image
    image = cv2.imread(in_file('milk_pack.jpg'))

    # Resize the image to 25% of the original size
    image = cv2.resize(image, None, fx=0.15, fy=0.15)

    # Converter a imagem para HSV
    imagem_hsv = cv2.cvtColor(image, cv2.COLOR_BGR2HSV)

    # Criar uma janela e definir a função de callback do mouse
    cv2.namedWindow('Imagem')
    cv2.setMouseCallback('Imagem', obter_valor_hue)

    while True:
        cv2.imshow('Imagem', image)
        if cv2.waitKey(1) & 0xFF == ord('q'):
            break

    cv2.destroyAllWindows()


if __name__ == '__main__':
    main()
"""
""" APAGAR
if __name__ == '__main__':
    image_directory = os.path.join('data', 'pi-images', 'outside')
    image_filename = 'milk2.jpg'
    image_path = os.path.join(image_directory, image_filename)
    #convert_rgb_to_hsv(image_path)
    most_present_h_value(in_file('milk_pack.jpg'))
"""


# Inside Algorithm (Hall)