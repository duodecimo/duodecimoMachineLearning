import numpy as np
import math as mt
from dictFromCifar import loadData

def readcats():

    train_x = np.load("catsori/trainxsaved.npy")
    train_y = np.load("catsori/trainysaved.npy")
    test_x = np.load("catsori/testxsaved.npy")
    test_y = np.load("catsori/testysaved.npy")

    return (train_x, train_y, test_x, test_y)

def giveMeCats(origin="CIFAR", ntrain=-1, ntest=-1, propcats=0.5):

    if origin == "COURSE":
        #from course
        (train_x, train_y, test_x, test_y) = readcats()

    else: 
        #from cifar
        train_x, train_y, test_x, test_y, labels = loadData()
        #As we are willing to identify cats (label = 3), we must have train_y and test_y
        ## as boolean vectors, where class cat = True, other classes = False
        train_y = np.int64(train_y==3)
        test_y = np.int64(test_y==3)

    #print("test_y: ", test_y.shape, test_y)

    if ntrain>0 :
        # let's obtain a only cats list
        #print("train shapes", train_x.shape, train_y.shape)
        traincats_x = train_x.T[(train_y>0).tolist()].T
        traincats_y = train_y.T[(train_y>0).tolist()].T
        #print("traincats_x shape", traincats_x.shape)
        #print("traincats_y shape", traincats_y.shape)

        if 1.0>=propcats>0.0:
            trunc = mt.ceil(ntrain*propcats)
            #print("truncar para : ", trunc)
            # let's truncate to ntrain*propcats
            traincats_x = traincats_x[:,0:trunc]
            traincats_y = traincats_y[:,0:trunc]
            #print("traincats_x shape", traincats_x.shape)
            #print("traincats_y shape", traincats_y.shape)


        # now a noncats list
        #print(train_x.shape, train_y.shape)
        trainno_x = train_x.T[(train_y==0).tolist()].T
        trainno_y = train_y.T[(train_y==0).tolist()].T
        #print("trainno_x shape", trainno_x.shape)
        #print("trainno_y shape", trainno_y.shape)
        trunc = ntrain - mt.ceil(ntrain*propcats)
        #print("truncar para : ", trunc)
        # let's truncate to ntrain*propcats
        trainno_x = trainno_x[:,0:trunc]
        trainno_y = trainno_y[:,0:trunc]
        #print("trainno_x shape", trainno_x.shape)
        #print("trainno_y shape", trainno_y.shape)
        # concatenate cats and nocats
        train_x = np.concatenate((traincats_x, trainno_x), axis = 1)
        train_y = np.concatenate((traincats_y, trainno_y), axis = 1)
        #print("train_x shape", train_x.shape)
        #print("train_y shape", train_y.shape)

    if ntest>0 :
        # finally truncate test
        test_x = test_x[:, 0:ntest]
        test_y = test_y[:, 0:ntest]
        #print("test_x shape", test_x.shape)
        #print("test_y shape", test_y.shape)

    ## Standardize data to have feature values between 0 and 1
    train_x = train_x/255
    test_x = test_x/255


    return (train_x, train_y, test_x, test_y)
  
#(train_x, train_y, test_x, test_y) = giveMeCats(origin="COURSE", ntrain=20, ntest=10, propcats=0.4)
