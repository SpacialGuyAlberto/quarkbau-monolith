import numpy as np

approx = np.array([
    [[0, 0]],
    [[10, 0]],
    [[10, 10]],
    [[9, 10]],
    [[9, 1]],
    [[0, 1]]
])

max_dist = -1
idx1, idx2 = 0, 0
for i in range(len(approx)):
    for j in range(i+1, len(approx)):
        dist = np.linalg.norm(approx[i][0] - approx[j][0])
        if dist > max_dist:
            max_dist = dist
            idx1 = i
            idx2 = j

print("idx1:", idx1, "idx2:", idx2)
path1 = approx[idx1:idx2+1]
path2 = np.concatenate((approx[idx2:], approx[:idx1+1]))
print("path1:", [p[0].tolist() for p in path1])
print("path2:", [p[0].tolist() for p in path2])
