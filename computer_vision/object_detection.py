import cv2
import matplotlib.pyplot as plt
from utils import in_file, out_file
import numpy as np

image = cv2.imread(in_file('plastic_bag.jpg'))
# Resize the image to 25% of the original size
img = cv2.resize(image, None, fx=0.25, fy=0.25)
imgGray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
cv2.imshow('image_gray', imgGray)

"""
# Histogramas das três componentes de cor
color = ('b', 'g', 'r')
for i, col in enumerate(color):
    histr = cv2.calcHist([img], [i], None, [256], [0, 256])
    plt.plot(histr, color=col)
    plt.xlim([0, 256])
plt.show()
"""

nbins = 256
binsInt = np.arange(nbins + 1)
hist, bin_edges = np.histogram(imgGray, bins=binsInt)
width = 0.35
p1 = plt.bar(binsInt[:-1], hist, width, color='r')
h = np.argmax(hist)
plt.show()

s = 0

# Iterar sobre o histograma ao longo do eixo x, da direita para a esquerda
for i in range(h, -1, -1):
    bin_count = hist[i]
    bin_left = bin_edges[i]
    bin_right = bin_edges[i + 1]

    if bin_count < 1500:
        print("Bin", i, ":", bin_left, "-", bin_right, ":", bin_count)
        s = bin_left
        break

"""
# Define os limites do intervalo
lower_threshold = 132
upper_threshold = 190

# Aplica a limiarização
_, thresholded_image1 = cv2.threshold(imgGray, lower_threshold, 255, cv2.THRESH_BINARY)
_, thresholded_image2 = cv2.threshold(imgGray, upper_threshold, 255, cv2.THRESH_BINARY_INV)

# Combina as imagens binárias resultantes usando uma operação lógica
result_image = cv2.bitwise_and(thresholded_image1, thresholded_image2)
thresholded_image = cv2.bitwise_not(result_image)  # Inverte a imagem binária
"""

print("O H é: ", h)
thres, bw = cv2.threshold(imgGray, s, 255, cv2.THRESH_BINARY)


cv2.imshow('image', bw)
cv2.waitKey(0)
cv2.destroyAllWindows()
