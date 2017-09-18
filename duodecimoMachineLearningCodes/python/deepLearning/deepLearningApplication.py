import time
import numpy as np
import scipy
from PIL import Image
from scipy import ndimage
from deepLearningUtils import *
from dictFromCifar import *

np.random.seed(1)

origtrain_x, train_y, origtest_x, test_y, classes = loadData("https://www.cs.toronto.edu/~kriz/cifar-10-python.tar.gz", "/media/duo/extra/python/tests/data/CIFAR-10/")

#reduce examples and tests
origtrain_x = origtrain_x[0:,0:400]
train_y = train_y[0:,0:400]
origtest_x = origtest_x[0:,0:50]
test_y = test_y[0:,0:50]

# Standardize data to have feature values between 0 and 1
train_x = origtrain_x/255
test_x = origtest_x/255

# Explore your dataset
m_train = train_x.shape[1]
num_px = train_x.shape[0]
m_test = test_x.shape[1]

print("Number of training examples: " + str(m_train))
print("Number of testing examples: " + str(m_test))
print("train_x shape: " + str(train_x.shape))
print("train_y shape: " + str(train_y.shape))
print("test_x shape: " + str(test_x.shape))
print("test_y shape: " + str(test_y.shape))
print("classes: ", classes)

# build and test a 2 layer neural network
### CONSTANTS DEFINING THE MODEL ####
n_x = 3072
# num_px * num_px * 3
n_h = 7
n_y = 1
layers_dims = (n_x, n_h, n_y)

def two_layer_model(X, Y, layers_dims, learning_rate = 0.0075, num_iterations = 3000, print_cost=False):
  """
  Implements a two-layer neural network: LINEAR->RELU->LINEAR->SIGMOID.
  Arguments:
  X -- input data, of shape (n_x, number of examples)
  Y -- true "label" vector (containing 0 if cat, 1 if non-cat), of shape (1, number of examples)
  layers_dims -- dimensions of the layers (n_x, n_h, n_y)
  num_iterations -- number of iterations of the optimization loop
  learning_rate -- learning rate of the gradient descent update rule
  print_cost -- If set to True, this will print the cost every 100 iterations
  Returns:
  parameters -- a dictionary containing W1, W2, b1, and b2
  """
  np.random.seed(1)
  grads = {}
  costs = []
  # to keep track of the cost
  m = X.shape[1]
  (n_x, n_h, n_y) = layers_dims
  # number of examples
  # Initialize parameters dictionary, by calling one of the functions you'd previously implemented
  ### START CODE HERE ### (≈ 1 line of code)
  parameters = initialize_parameters(layers_dims)
  ### END CODE HERE ###
  # Get W1, b1, W2 and b2 from the dictionary parameters.
  W1 = parameters["W1"]
  b1 = parameters["b1"]
  W2 = parameters["W2"]
  b2 = parameters["b2"]
  # Loop (gradient descent)
  for i in range(0, num_iterations):
    # Forward propagation: LINEAR -> RELU -> LINEAR -> SIGMOID. Inputs: "X, W1, b1". Output: "A1, cache1, A2, cache2".
    ### START CODE HERE ### (≈ 2 lines of code)
    A1, cache1 = linear_activation_forward(X, W1, b1, "relu")
    A2, cache2 = linear_activation_forward(A1, W2, b2, "sigmoid")
    ### END CODE HERE ###
    # Compute cost
    ### START CODE HERE ### (≈ 1 line of code)
    cost = compute_cost(A2, Y)
    ### END CODE HERE ###
    # Initializing backward propagation
    dA2 = - (np.divide(Y, A2) - np.divide(1 - Y, 1 - A2))
    # Backward propagation. Inputs: "dA2, cache2, cache1". Outputs: "dA1, dW2, db2; also dA0 (not used), dW1, db1".
    ### START CODE HERE ### (≈ 2 lines of code)
    dA1, dW2, db2 = linear_activation_backward(dA2, cache2, "sigmoid")
    dA0, dW1, db1 = linear_activation_backward(dA1, cache1, "relu")
    ### END CODE HERE ###
    # Set grads['dWl'] to dW1, grads['db1'] to db1, grads['dW2'] to dW2, grads['db2'] to db2
    grads['dW1'] = dW1
    grads['db1'] = db1
    grads['dW2'] = dW2
    grads['db2'] = db2
    # Update parameters.
    ### START CODE HERE ### (approx. 1 line of code)
    parameters = update_parameters(parameters, grads, learning_rate)
    ### END CODE HERE ###
    # Retrieve W1, b1, W2, b2 from parameters
    W1 = parameters["W1"]
    b1 = parameters["b1"]
    W2 = parameters["W2"]
    b2 = parameters["b2"]
    # Print the cost every 100 training example
    if print_cost and i % 100 == 0:
      print("Cost after iteration {}: {}".format(i, np.squeeze(cost)))
    if print_cost and i % 100 == 0:
      costs.append(cost)
  return parameters

parameters = two_layer_model(train_x, train_y, layers_dims = (n_x, n_h, n_y), num_iterations = 2500, print_cost=True)

