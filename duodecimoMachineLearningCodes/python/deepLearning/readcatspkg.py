import numpy as np

def readcats():

  train_x = np.load("catsori/trainxsaved.npy")
  train_y = np.load("catsori/trainysaved.npy")
  test_x = np.load("catsori/testxsaved.npy")
  test_y = np.load("catsori/testysaved.npy")

  return (train_x, train_y, test_x, test_y)
  
