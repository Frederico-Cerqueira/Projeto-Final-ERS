import subprocess
import time

def enviar_comando(comando):
    subprocess.run(comando, shell=True)
    time.sleep(0.1)  # Aguarda 1 segundo entre os comandos (opcional)

# Lista de comandos a serem enviados
comandos = [
    "MOVE 500",
    "HEAD 90",
    "MOVE 500"
    # Adicione mais comandos conforme necessário
]

# Loop para enviar os comandos
for comando in comandos:
    enviar_comando(comando)