import cv2
import numpy as np
import os

img_path = 'mi_calle.png'
out_path = 'mi_calle_plano.png'

if not os.path.exists(img_path):
    print("Error: Image not found.")
    exit(1)

img = cv2.imread(img_path)
h, w = img.shape[:2]

# Draw the main red line (Trasse)
# Based on the screenshot: Starts top right, curves left, goes down
points = np.array([
    [int(w * 0.9), int(h * 0.15)],
    [int(w * 0.25), int(h * 0.25)],
    [int(w * 0.32), int(h * 0.85)]
], np.int32)

points = points.reshape((-1, 1, 2))

# Draw red polyline
cv2.polylines(img, [points], False, (0, 0, 255), thickness=int(w*0.015))

# Draw some blue branches (Acometidas) towards the buildings (Zahntechnik, etc)
branches = [
    ((int(w * 0.5), int(h * 0.21)), (int(w * 0.5), int(h * 0.35))),
    ((int(w * 0.28), int(h * 0.45)), (int(w * 0.55), int(h * 0.45))), # towards Fürst Zahntechnik
    ((int(w * 0.30), int(h * 0.6)), (int(w * 0.6), int(h * 0.6))),
    ((int(w * 0.31), int(h * 0.75)), (int(w * 0.7), int(h * 0.75)))
]

for start, end in branches:
    cv2.line(img, start, end, (255, 0, 0), thickness=int(w*0.005))
    
# Add some text labels
cv2.putText(img, "LWL-Trasse (Geplant)", (int(w*0.35), int(h*0.13)), cv2.FONT_HERSHEY_SIMPLEX, 0.8, (0, 0, 255), 2)
cv2.putText(img, "HA", (int(w*0.57), int(h*0.45)), cv2.FONT_HERSHEY_SIMPLEX, 0.6, (255, 0, 0), 2)

cv2.imwrite(out_path, img)
print("Image saved successfully.")
