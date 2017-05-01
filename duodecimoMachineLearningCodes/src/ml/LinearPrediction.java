/*
 * Copyright (C) 2017 duo
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ml;

import cifar10.Cifar10Utils;
import java.io.IOException;
import static java.lang.Double.max;
import java.lang.reflect.Array;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.DoubleStream;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
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

    /**
     *
     * @throws IOException
     */
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

    /**
     * Assume X_test is [3073 x 10000] (bias trick applied), Y_test [10000 x 1].
     * scores = Wbest.dot(Xte_cols)
     * 10 x 10000, the class scores for all test examples.
     * find the index with max score in each column (the predicted class)
     * Yte_predict = np.argmax(scores, axis = 0).
     * and calculate accuracy (fraction of predictions that are correct)
     * np.mean(Yte_predict == Yte)
     * finds ~0.1555, round 15%      
     */
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
    }

    /**
     * Compute the multiclass svm loss for a single example (x,y)
     * 
     * @param x a column vector representing an image (e.g. 3073 x 1 in CIFAR-10)
     * @param y is an integer giving index of correct class
     * (e.g. between 0 and 9 in CIFAR-10)
     * @param W is the weight matrix (e.g. 10 x 3073 in CIFAR-10)
     * @return the loss as a float
     */
    public float lossFunctionUnvectorized(RealVector x, int y, RealMatrix W) {
        float delta = 1.0f;
        RealVector scores = W.operate(x);
        double correctClassScore = scores.getEntry(y);
        int d = W.getRowDimension();
        float loss = 0.0f;
        // iterate over all wrong classes
        for(int i=0; i<d; i++) {
            if(i == correctClassScore) {
                continue;
            }
            loss += Double.max(0, scores.getEntry(i) - 
                    correctClassScore + delta);
        }
        return loss;
    } 

    /**
     * Compute the multiclass svm loss for a single example (x,y)
     * 
     * A faster half-vectorized implementation.
     * half-vectorized  refers to the fact that for a single example the 
     * implementation contains no for loops, but there is still one loop
     * over the examples (outside this function)
     * 
     * @param x a column vector representing an image (e.g. 3073 x 1 in CIFAR-10)
     * @param y is an integer giving index of correct class
     * (e.g. between 0 and 9 in CIFAR-10)
     * @param W is the weight matrix (e.g. 10 x 3073 in CIFAR-10)
     * @return the loss as a float
     */
    public double lossFunctionSemivectorized(RealVector x, int y, RealMatrix W) {
        float delta = 1.0f;
        // scores becomes of size 10 x 1, the scores for each class
        RealVector scores = W.operate(x);
        // compute the margins for all classes in one vector operation
        /*
        margins = np.maximum(0, scores - scores[y] + delta)
        on y-th position scores[y] - scores[y] canceled and gave delta. We want
        to ignore the y-th position and only consider margin on max wrong class
        margins[y] = 0
        loss_i = np.sum(margins)
        return loss_i
        */
        RealVector margins;
        margins = scores.mapSubtract(scores.getEntry(y)).mapAdd(delta);
        margins.mapToSelf(new Maximum());
        margins.setEntry(y, 0.0d);
        double loss = margins.dotProduct(margins.map(new Ones()));
        return loss;
    }

    private static class Maximum implements UnivariateFunction {
        @Override
        public double value(double x) {
            return max(0.0d, x);
        }
    }

    private static class Ones implements UnivariateFunction {
        @Override
        public double value(double x) {
            return 1.0d;
        }
    }

    /**
     *
     * @param args command line parameters if needed.
     * @throws IOException if received from class,
     */
    public static void main(String[] args) throws IOException {
        LinearPrediction linearPrediction = new LinearPrediction();
    }}
