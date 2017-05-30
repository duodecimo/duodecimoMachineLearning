# A bit of setup
import numpy as np
#import matplotlib.pyplot as plt


np.random.seed(0)
N = 100 # number of points per class
D = 2 # dimensionality
K = 3 # number of classes

# using np.sum
# explain probs = exp_scores / np.sum(exp_scores, axis=1, keepdims=True) # [N x K]

k = np.array([[1.0, 2.0, 3.0],[4.0, 5.0, 6.0]])
print("k:"),
print(type(k)),
print(k.shape)
print(k)
print("k.shape[0]: "),
print(k.shape[0])

l = np.sum(k, axis=1, keepdims=True)

print("l:"),
print(type(l)),
print(l.shape)
print(l)

m = k/l

print("m:"),
print(type(m)),
print(m.shape)
print(m)
y=np.ones(6, dtype='uint8');
ct = np.log(k[range(6),y])

