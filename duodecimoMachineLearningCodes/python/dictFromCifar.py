import numpy as np

######## python3 #########
def unpickle(file):
    import pickle
    with open(file, 'rb') as fo:
        dict = pickle.load(fo, encoding='bytes')
    return dict

######## python2 #########
#def unpickle(file):
#    import cPickle
#    with open(file, 'rb') as fo:
#        dict = cPickle.load(fo)
#    return dict

dict = unpickle("data/CIFAR-10/cifar-10-batches-py/data_batch_1");

print("\ndict keys: ", dict.keys())

X = dict[b'data']

Y = dict[b'labels']

dict = unpickle("data/CIFAR-10/cifar-10-batches-py/data_batch_2");

X = np.concatenate((X, dict[b'data'])) 

Y = np.concatenate((Y, dict[b'labels']))

dict = unpickle("data/CIFAR-10/cifar-10-batches-py/data_batch_3");

X = np.concatenate((X, dict[b'data'])) 

Y = np.concatenate((Y, dict[b'labels']))

dict = unpickle("data/CIFAR-10/cifar-10-batches-py/data_batch_4");

X = np.concatenate((X, dict[b'data'])) 

Y = np.concatenate((Y, dict[b'labels']))

dict = unpickle("data/CIFAR-10/cifar-10-batches-py/data_batch_5");

X_train = np.concatenate((X, dict[b'data'])).T 

Y_train = np.concatenate((Y, dict[b'labels']))

dict = unpickle("data/CIFAR-10/cifar-10-batches-py/test_batch");

X_test = dict[b'data'].T

Y_test = np.array(dict[b'labels'])

Y_train = np.reshape(Y_train, (1, len(Y_train)))

Y_test = np.reshape(Y_test, (1, len(Y_test)))

print("\nX_train.shape: ", X_train.shape);

print("\nY_train.shape: ", Y_train.shape);

print("\nX_test.shape : ", X_test.shape);

print("\nY_test.shape : ", Y_test.shape);

