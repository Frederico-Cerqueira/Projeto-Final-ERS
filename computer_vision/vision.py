import cv2
import numpy as np
import time


COLORS = [(0, 255, 255), (255, 255, 0), (255, 0, 255), (255, 0, 0), (0, 255, 0), (0, 0, 255)]

class_names = []
with open('coco.names', 'r') as f:
    class_names = [cname.strip() for cname in f.readlines()]

cap = cv2.VideoCapture(0)

net = cv2.dnn.readNet("yolov3.weights", "yolov3.cfg")
model = cv2.dnn.DetectionModel(net)
model.setInputParams((1 / 250), (416, 416))

while True:
    ret, frame = cap.read()
    start = time.time()
    if frame is None or frame.shape[0] == 0 or frame.shape[1] == 0:
        print("Error: Invalid frame dimensions")
        break  # Or handle the error appropriately

    classes, scores, boxes = model.detect(frame, 0.1, 0.2)
    end = time.time()

    for (classid, score, box) in zip(classes, scores, boxes):
        color = COLORS[int(classid) % len(COLORS)]

        label = f"{class_names[classid]} : {score}"

        cv2.rectangle(frame, box, color, 2)
        cv2.putText(frame, label, (box[0], box[1] - 10), cv2.FONT_HERSHEY_SIMPLEX, 0.5, color, 2)

    fps_label = f"FPS: {round((1.0 / (end - start)), 2)}"
    cv2.putText(frame, fps_label, (0, 25), cv2.FONT_HERSHEY_SIMPLEX, 1, (0, 0, 0), 5)
    cv2.putText(frame, fps_label, (0, 25), cv2.FONT_HERSHEY_SIMPLEX, 1, (0, 255, 0), 3)

    cv2.imshow("detection", frame)

    if cv2.waitKey(1) == 27:
        break

cap.release()
cv2.destroyAllWindows()