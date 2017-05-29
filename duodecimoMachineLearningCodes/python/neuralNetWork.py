import numpy as np
N=100
K=3
D=2

X = np.zeros((N*K,D))
y = np.zeros(N*K, dtype='uint8')
for j in xrange(K):
  ix = range(N*j,N*(j+1))
  r = np.linspace(0.0,1,N) # radius
  t = np.linspace(j*4,(j+1)*4,N) + np.random.randn(N)*0.2 # theta
  X[ix] = np.c_[r*np.sin(t), r*np.cos(t)]
  y[ix] = j


W = 0.01 * np.random.randn(D,K)
b = np.zeros((1,K))
print("W:"),
print(type(W)),
print(W.shape)
print(W)
print("\nb:"),
print(type(b)),
print(b.shape)
print(b)

scores = np.dot(X, W) + b
print("\nscores:"),
print(type(scores)),
print(scores.shape)
print(scores)


#Z = np.ones((N, K))
#b = np.ones((1, K));

#print("\n\nZ+b:")
#print(Z+b)


#Train a Linear Classifier

# initialize parameters randomly
W = 0.01 * np.random.randn(D,K)
b = np.zeros((1,K))

# some hyperparameters
step_size = 1e-0
reg = 1e-3 # regularization strength

# gradient descent loop
num_examples = X.shape[0]

scores = np.dot(X, W) + b 
 
# compute the class probabilities
exp_scores = np.exp(scores)
probs = exp_scores / np.sum(exp_scores, axis=1, keepdims=True) # [N x K]

print("type(scores): ")
print(type(scores)),
print(scores.shape)
print("type(exp_scores): ")
print(type(exp_scores)),
print(exp_scores.shape)

a = np.sum(exp_scores, axis=1, keepdims=True)
print("type(a): ")
print(type(a)),
print(a.shape)

print("type(probs): ")
print(type(probs)),
print(probs.shape)
print("probs");
print(probs);

print("type(probs[range(num_examples),y]): "),
print(type(probs[range(num_examples),y])),
print(probs[range(num_examples),y].shape)
print("probs[range(num_examples),y]: ")
print(probs[range(num_examples),y])


for i in xrange(200):
  
  # evaluate class scores, [N x K]
  scores = np.dot(X, W) + b 
  
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
  dscores[range(num_examples),y] -= 1
  dscores /= num_examples
  
  # backpropate the gradient to the parameters (W,b)
  dW = np.dot(X.T, dscores)
  db = np.sum(dscores, axis=0, keepdims=True)
  
  dW += reg*W # regularization gradient
  
  # perform a parameter update
  W += -step_size * dW
  b += -step_size * db

