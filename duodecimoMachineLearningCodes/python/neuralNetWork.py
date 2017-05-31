import numpy as np
N=100
K=3
D=2

X = np.zeros((N*K,D))
y = np.zeros(N*K, dtype='uint8')

# initialize W randomly
#W = 0.01 * np.random.randn(D,K)

# alternative: fixed initial W
W = np.array([[0.01, 0.0, -0.01],[0.0, -0.01, 0.01]])

b = np.zeros((1,K))

print("W:"),
print(type(W)),
print(W.shape)
print(W)
print("\nb:"),
print(type(b)),
print(b.shape)
print(b)


for j in xrange(K):
  ix = range(N*j,N*(j+1))
  r = np.linspace(0.0,1,N) # radius
  t = np.linspace(j*4,(j+1)*4,N) + np.random.randn(N)*0.2 # theta
  X[ix] = np.c_[r*np.sin(t), r*np.cos(t)]
  y[ix] = j

# numpy.savetxt(fname, X, fmt='%.18e', delimiter=' ', newline='\n', header='', footer='', comments='# ')
#np.savetxt("Xdata.txt", X, delimiter = ', ')
#np.savetxt("Ydata.txt", y, delimiter = ', ')

# numpy.loadtxt(fname, dtype=<type 'float'>, comments='#', delimiter=None, converters=None, skiprows=0, usecols=None, unpack=False, ndmin=0)
X = np.loadtxt("Xdata.txt", delimiter = ', ')
y = np.loadtxt("Ydata.txt", dtype='uint8', delimiter = ', ')

scores = np.dot(X, W) + b


#Z = np.ones((N, K))
#b = np.ones((1, K));

#print("\n\nZ+b:")
#print(Z+b)


#Train a Linear Classifier


# some hyperparameters
step_size = 1e-0
reg = 1e-3 # regularization strength

# gradient descent loop
num_examples = X.shape[0]

# print("type(exp_scores): ")
# print(type(exp_scores)),
# print(exp_scores.shape)

# a = np.sum(exp_scores, axis=1, keepdims=True)
# print("type(a): ")
# print(type(a)),
# print(a.shape)

# print("type(probs): ")
# print(type(probs)),
# print(probs.shape)
# print("probs");
# print(probs);

# print("type(probs[range(num_examples),y]): "),
# print(type(probs[range(num_examples),y])),
# print(probs[range(num_examples),y].shape)
# print("probs[range(num_examples),y]: ")
# print(probs[range(num_examples),y])


for i in xrange(200):
  
  # evaluate class scores, [N x K]
  scores = np.dot(X, W) + b

  if i<1:
    print("\nscores:"),
    print(type(scores)),
    print(scores.shape)
    print(scores)
  
  # compute the class probabilities
  exp_scores = np.exp(scores)
  probs = exp_scores / np.sum(exp_scores, axis=1, keepdims=True) # [N x K]

  # compute the loss: average cross-entropy loss and regularization
  corect_logprobs = -np.log(probs[range(num_examples),y])
  data_loss = np.sum(corect_logprobs)/num_examples
  reg_loss = 0.5*reg*np.sum(W*W)
  loss = data_loss + reg_loss
  if i % 10 == 0:
    print "iteration %d: loss %f" % (i, loss)
  
  # compute the gradient on scores
  dscores = probs

  #print("1: dscores: "),
  #print(type(dscores)),
  #print(dscores.shape)
  #print(dscores)

	# minus one from correct class columns
  dscores[range(num_examples),y] -= 1

  #print("2: dscores: "),
  #print(type(dscores)),
  #print(dscores.shape)
  #print(dscores)

  dscores /= num_examples

  #print("3: dscores: "),
  #print(type(dscores)),
  #print(dscores.shape)
  #print(dscores)

  #print("np.sum(dscores, axis=0, keepdims=True): "),
  #print(type(np.sum(dscores, axis=0, keepdims=True))),
  #print(np.sum(dscores, axis=0, keepdims=True))

  # backpropate the gradient to the parameters (W,b)
  dW = np.dot(X.T, dscores)
  db = np.sum(dscores, axis=0, keepdims=True)
  
  dW += reg*W # regularization gradient
  
  # perform a parameter update
  W += -step_size * dW
  b += -step_size * db

