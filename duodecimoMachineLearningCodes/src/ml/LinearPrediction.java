/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml;

import cifar10.Cifar10Utils;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.DoubleStream;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.random.JDKRandomGenerator;

/**
 *
 * @author duo
 */
public class LinearPrediction {
        private final RealMatrix Xtr, Ytr, Xte, Yte;
        private final Cifar10Utils cifar10Utils;
        private static final Logger LOGGER = Logger.getGlobal();
    public LinearPrediction() throws IOException {
        LOGGER.setLevel(Level.INFO);
        /**
         * The class cifar10.Cifar10Utils from this package is well documented
         * and it is strongly recomended the reading of its comments explainning
         * how it works, what it does and why before going further.
        */
        cifar10Utils = new Cifar10Utils();
        LOGGER.log(Level.INFO, "Running Linear Prediction".concat(
                String.format("(using %4.2f %% of dataset)", 
                        cifar10Utils.getLoadPercentual())));
        Xtr = cifar10Utils.getXtr();
        Xte = cifar10Utils.getXte();
        Ytr = cifar10Utils.getYtr();
        Yte = cifar10Utils.getYte();
        linearPredictionWithRandomSearch();
    }

    public final void linearPredictionWithRandomSearch() {
        RealMatrix BestW = null; // to hold the best random generated weights
        float bestloss = Float.MAX_VALUE, loss;
        boolean checkWeights = true; // lets just display some generated gaussian weights
        boolean checkTraining = true; // lets just display some trainnings
        for(int i=0; i<500; i++) { // number of guesses
            DoubleStream doubleStream = new JDKRandomGenerator((int) System.currentTimeMillis()).
                    doubles((cifar10Utils.getTotalOfBytes()+1) * cifar10Utils.getNames().length);
            double[] doubles = doubleStream.toArray();
            // generate random W gaussian
            RealMatrix W = MatrixUtils.createRealMatrix(cifar10Utils.getNames().length,
                    cifar10Utils.getTotalOfBytes()+1);
            int k=0;
            for(int row=0; row<cifar10Utils.getNames().length; row++) {
                for(int col=0; col<cifar10Utils.getTotalOfBytes()+1;col++) {
                    W.setEntry(row, col, doubles[k++]);
                }
            }
            if(checkWeights) {
                System.out.println("Checking some weigths");
                for(int wl=0; wl<10;wl++) { //lines
                    // inital cols
                    for(int wc=0;wc<8;wc++) {
                        System.out.print(String.format("%8.6f ", W.getEntry(wl, wc)));
                    }
                    System.out.print(" ... ");
                    // final cols
                    for(int wc=W.getColumnDimension()-8;wc<W.getColumnDimension();wc++) {
                        System.out.print(String.format("%8.6f ", W.getEntry(wl, wc)));
                    }
                    System.out.println("");
                }
                checkWeights=false;
            }
            loss=0.0f;
            for (int j = 0; j < cifar10Utils.getTotalOfTrainnings(); j++) {
                // loop to visit all trainnings
                // notice below that we need to append a 1 to the end of the image 
                // vector for the bias trick
                int index = (int) Ytr.getEntry(j, 0);
                if(checkTraining) {
                    // wow, appears that all bias = 0,whats wrong with append(1.0d) ?
                    System.out.println("Checking some trainnings vector size before bias: " +
                            Xtr.getRowVector(j).getDimension());
                        for(int wc=0;wc<5;wc++) {
                            //inicio
                            System.out.print(String.format("%6.4f ", Xtr.getRowVector(j).getEntry(wc)));
                        }
                        System.out.print(" ... ");
                        for(int wc=Xtr.getRowVector(j).getDimension()-5;wc<Xtr.getRowVector(j).getDimension();wc++) {
                            //fim
                            System.out.print(String.format("%6.4f ", Xtr.getRowVector(j).getEntry(wc)));
                        }
                        System.out.println("");
                    checkTraining=true;
                }
                RealVector x = Xtr.getRowVector(j);
                x = x.append(1.0d);
                // it seems it is not appending 1.0d, but 0.0d, bug?
                // lets try to rewrite tomake sure
                x.setEntry(x.getDimension()-1, 1.0d);
                if(checkTraining) {
                    // wow, appears that all bias = 0,whats wrong with append(1.0d) ?
                    System.out.println("Checking some trainnings vector size after bias: " +
                            x.getDimension());
                        for(int wc=0;wc<5;wc++) {
                            //inicio
                            System.out.print(String.format("%6.4f ", x.getEntry(wc)));
                        }
                        System.out.print(" ... ");
                        for(int wc=x.getDimension()-6;wc<x.getDimension();wc++) {
                            //fim
                            System.out.print(String.format("%6.4f ", x.getEntry(wc)));
                        }
                        System.out.println("");
                    checkTraining=false;
                }
                loss += lossFunctionUnvectorized(x, (int) index, W);
            }
            if(loss<bestloss) {
                bestloss = loss;
                BestW = W.copy();
            }
            System.out.println(String.format("in attempt %d the loss was %f, "
                    + "best %f %c", i+1, loss, bestloss, '%'));
        }
        // BestW holds the weigths
            RealVector test;
            double groundLabel;
            RealVector scores;
            double predictLable;
            int accuracy = 0;
            int numberOfTestings = cifar10Utils.getTotalOfTests();
        for(int k=0; k< numberOfTestings; k++) {
            //lets visit all tests
            test = Xte.getRowVector(k).append(1d);
            groundLabel = Yte.getEntry(k, 0);
            /*            System.out.println(String.format("BestW(%d, %d).operate(test(%d):",
            BestW.getRowDimension(), BestW.getColumnDimension(),
            test.getDimension()));*/
            scores = BestW.operate(test);
            predictLable = scores.getMaxIndex();
            if(predictLable == groundLabel) {
                accuracy++;
            }
            System.out.println(String.format("test %d predicted = %f  ground = %f", 
                    k, predictLable, groundLabel));
        }
        System.out.println(String.format("accuracy: %5.2f %%", 
                (float)(accuracy * 100 /numberOfTestings)));
/*
# Assume X_test is [3073 x 10000], Y_test [10000 x 1]
scores = Wbest.dot(Xte_cols) # 10 x 10000, the class scores for all test examples
# find the index with max score in each column (the predicted class)
Yte_predict = np.argmax(scores, axis = 0)
# and calculate accuracy (fraction of predictions that are correct)
np.mean(Yte_predict == Yte)
# returns 0.1555      
*/

    }

    public float lossFunctionUnvectorized(RealVector x, int y, RealMatrix W) {
        float delta = 1.0f;
        RealVector scores = W.operate(x);
        double correctClassScore = scores.getEntry(y);
        int d = W.getRowDimension();
        float loss = 0.0f;
        for(int i=0; i<d; i++) {
            if(i == correctClassScore) {
                continue;
            }
            loss += Double.max(0, scores.getEntry(i) - correctClassScore + delta);
        }
        return loss;
    } 

    public static void main(String[] args) throws IOException {
        LinearPrediction linearPrediction = new LinearPrediction();
    }}
