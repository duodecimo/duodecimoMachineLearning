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
import util.matrix.DuodecimoMatrixUtils;

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
        cifar10Utils = new Cifar10Utils(true);
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
        DuodecimoMatrixUtils.showRealMatrix("sampling Xtr", Xtr, 10, 10);
        // lets add a column of ones to Xtr in order to perform the bias trick
        RealMatrix XtrWithOnes = DuodecimoMatrixUtils.attachOnesColumn(Xtr);
        DuodecimoMatrixUtils.showRealMatrix("sampling XtrWithOnes", XtrWithOnes, 10, 11);
        RealMatrix BestW = null; // to hold the best random generated weights
        float bestloss = Float.MAX_VALUE, loss;
        boolean sampleFirstWeights = true;
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
            if (sampleFirstWeights) {
                DuodecimoMatrixUtils.showRealMatrix("sampling weights", W, -1, 10);
                sampleFirstWeights = !sampleFirstWeights;
            }
            loss=0.0f;
            for (int j = 0; j < cifar10Utils.getTotalOfTrainnings(); j++) {
                // loop to visit all trainnings
                int index = (int) Ytr.getEntry(j, 0);
                RealVector x = XtrWithOnes.getRowVector(j);
                loss += lossFunctionUnvectorized(x, (int) index, W);
            }
            if(loss<bestloss) {
                bestloss = loss;
                BestW = W.copy();
            }
            System.out.println(String.format("in guess attempt %d the loss was %f, "
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
