from picamera2 import Picamera2
from io import BytesIO
from PIL import Image
import numpy as np

def take_photo():
    # Inicializa a cï¿½mera
    picam2 = Picamera2()

    # Configura a cï¿½mera
    config = picam2.create_still_configuration()
    picam2.configure(config)
    picam2.start()

    # Captura a imagem em um buffer de memï¿½ria
    stream = BytesIO()
    picam2.capture_file(stream, format='jpeg')

    # Movendo o ponteiro do buffer para o inï¿½cio
    stream.seek(0)

    # Carrega a imagem em uma variï¿½vel usando PIL
    image = Image.open(stream)

    # Converte a imagem do PIL para numpy array
    image_np = np.array(image)

    """ APAGAR
    # Desenha um retï¿½ngulo na imagem usando OpenCV
    image_np = cv2.rectangle(image_np, (0, 0), (image_np.shape[1], image_np.shape[0]), (255, 0, 0), 2)
    cv2.imshow('image', image_np)
    # Converte de volta para PIL Image para salvar como JPEG
    image_with_rectangle = Image.fromarray(image_np)
    # Salva a imagem com retï¿½ngulo como um arquivo JPEG
    image_with_rectangle.save('imagem_com_retangulo.jpeg', 'JPEG')

    # Opcional: para visualizar a imagem armazenada com retï¿½ngulo
    image_with_rectangle.show()
    """
    # Para garantir que os recursos da cï¿½mera sejam liberados
    picam2.stop()
    return image_np