import cv2
import numpy as np

img = np.zeros((100, 100, 3), dtype=np.uint8)
cv2.line(img, (10, 10), (90, 10), (0, 0, 255), 5) # thick horizontal line
cv2.line(img, (90, 10), (90, 90), (0, 0, 255), 5) # thick vertical line

# Color filter red
lower_red = np.array([0, 0, 200])
upper_red = np.array([50, 50, 255])
mask = cv2.inRange(img, lower_red, upper_red)

# Skeletonize
size = np.size(mask)
skel = np.zeros(mask.shape, np.uint8)
img_temp = mask.copy()
element = cv2.getStructuringElement(cv2.MORPH_CROSS, (3,3))
for _ in range(100):
    eroded = cv2.erode(img_temp, element)
    temp = cv2.dilate(eroded, element)
    temp = cv2.subtract(img_temp, temp)
    skel = cv2.bitwise_or(skel, temp)
    img_temp = eroded.copy()
    if cv2.countNonZero(img_temp) == 0:
        break

contours, _ = cv2.findContours(skel, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)
print("Found contours:", len(contours))
for c in contours:
    approx = cv2.approxPolyDP(c, 2.0, True)
    print("Contour points:", len(approx))
