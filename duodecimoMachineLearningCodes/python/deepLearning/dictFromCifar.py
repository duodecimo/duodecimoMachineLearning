import numpy as np
from download import *

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


def loadData(url, data_dir):
    """
    Download and extract the data if it doesn't already exist.
    Assumes the url is a tar-ball file.
    :param url:
        Internet URL for the tar-file to download.
        Example: "https://www.cs.toronto.edu/~kriz/cifar-10-python.tar.gz"
    :param data_dir:
        Directory where the downloaded file is saved.
        Example: "data/CIFAR-10/"
    :return:
        dict
    """

    # use function maybe_download_and_extract from download.py to download CIFAR-10 data
    maybe_download_and_extract(url, data_dir)

    # each data file unpickled from CIFAR-10 has dict keys:  dict_keys([b'batch_label', b'labels', b'data', b'filenames'])
    for i in range(4):
        # Load the images and class-numbers from the data-file.
        dict = unpickle(data_dir + "cifar-10-batches-py/data_batch_" + str(i + 1))
				# print("\ndict keys: ", dict.keys())
        if(i == 0):
            X = dict[b'data']
            Y = dict[b'labels']
        else:
            X = np.concatenate((X, dict[b'data'])) 
            Y = np.concatenate((Y, dict[b'labels']))

    X_train = np.concatenate((X, dict[b'data'])).T 
    Y_train = np.concatenate((Y, dict[b'labels']))

    dict = unpickle(data_dir + "cifar-10-batches-py/test_batch");
    X_test = dict[b'data'].T
    Y_test = np.array(dict[b'labels'])
    Y_train = np.reshape(Y_train, (1, len(Y_train)))
    Y_test = np.reshape(Y_test, (1, len(Y_test)))

    raw = unpickle(data_dir + "cifar-10-batches-py/batches.meta")[b'label_names']
    # Convert from binary strings.
    names = [x.decode('utf-8') for x in raw]

    return X_train, Y_train, X_test, Y_test, names

X_tr, Y_tr, X_te, Y_te, names = loadData("https://www.cs.toronto.edu/~kriz/cifar-10-python.tar.gz", "data/CIFAR-10/")
print("\nX_train shape: ", X_tr.shape);
print("\nY_train shape: ", Y_tr.shape);
print("\nX_test shape : ", X_te.shape);
print("\nY_test	shape : ", Y_te.shape);
print("\nNames : ", names);

