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
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.DoubleStream;
import org.apache.commons.math3.analysis.UnivariateFunction;
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
        // we can try
        //linearPredictionWithRandomSearch();
        // or else
        linearPredictionWithRandomLocalSearch();
    }

    /**
     * Assume X_test is [2073 x 10000] (bias trick applied), Y_test [10000 x 1].
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
        RealMatrix W;
        float bestloss = Float.MAX_VALUE, loss, loss2, loss3;
        boolean sampleFirstWeights = true;
        for(int i=0; i<500; i++) { // number of guesses
            DoubleStream doubleStream = new JDKRandomGenerator((int) System.currentTimeMillis()).
                    doubles((cifar10Utils.getTotalOfBytes()+1) * cifar10Utils.getNames().length);
            double[] doubles = doubleStream.toArray();
            // generate random W gaussian
            W = MatrixUtils.createRealMatrix(cifar10Utils.getNames().length,
                    cifar10Utils.getTotalOfBytes()+1);
            int k=0;
            for(int row=0; row<cifar10Utils.getNames().length; row++) {
                for(int col=0; col<cifar10Utils.getTotalOfBytes()+1;col++) {
                    W.setEntry(row, col, doubles[k++] * 0.001);
                }
            }
            if (sampleFirstWeights) {
                LOGGER.info(DuodecimoMatrixUtils.showRealMatrix("sampling weights", W, 6, 10));
                sampleFirstWeights = !sampleFirstWeights;
            }
            RealVector Y = Ytr.getColumnVector(0);
            loss3 = (float) lossFunctionFullvectorized(XtrWithOnes, Y, W);
            LOGGER.info("Loss (full vectorized calc) = ".concat(Double.toString(loss3)));
            loss2 = (float) lossFunctionSemivectorized(XtrWithOnes, Y, W);
            LOGGER.info("Loss (semi vectorized calc) = ".concat(Double.toString(loss2)));
            loss = (float) lossFunctionUnvectorized(XtrWithOnes, Y, W);
            LOGGER.info("Loss (unvectorized calc) = ".concat(Double.toString(loss)));
            if(loss<bestloss) {
                bestloss = loss;
                BestW = W.copy();
            }
            System.out.println(String.format("in guess attempt %d the loss was %f, "
                    + "best so far %f", i+1, loss, bestloss));
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
            System.out.println(String.format("test %d predicted = %f  ground = %f %c", 
                    k, predictLable, groundLabel, predictLable == groundLabel? '!' : ' '));
        }
        System.out.println(String.format("accuracy: %5.2f %%", 
                (float)(accuracy * 100 /numberOfTestings)));
    }

    /**
     * Assume X_test is [2073 x 10000] (bias trick applied), Y_test [10000 x 1].
     * scores = Wbest.dot(Xte_cols)
     * 10 x 10000, the class scores for all test examples.
     * find the index with max score in each column (the predicted class)
     * Yte_predict = np.argmax(scores, axis = 0).
     * and calculate accuracy (fraction of predictions that are correct)
     * np.mean(Yte_predict == Yte)
     * finds ~0.1555, round 15%      
     */
    public final void linearPredictionWithRandomLocalSearch() {
        DuodecimoMatrixUtils.showRealMatrix("sampling Xtr", Xtr, 10, 10);
        // lets add a column of ones to Xtr in order to perform the bias trick
        RealMatrix XtrWithOnes = DuodecimoMatrixUtils.attachOnesColumn(Xtr);
        RealVector Y = Ytr.getColumnVector(0);
        DuodecimoMatrixUtils.showRealMatrix("sampling XtrWithOnes", XtrWithOnes, 10, 11);
        RealMatrix BestW = null; // to hold the best random generated weights
        double stepSize = 0.001d;
        double bestloss = Double.MAX_VALUE, loss;
        boolean sampleFirstWeights = true;
        DoubleStream doubleStream = new JDKRandomGenerator((int) System.currentTimeMillis()).
                doubles((cifar10Utils.getTotalOfBytes()+1) * cifar10Utils.getNames().length);
        double[] doubles = doubleStream.toArray();
        // generate random W gaussian
        RealMatrix Wtry;
        RealMatrix W = MatrixUtils.createRealMatrix(cifar10Utils.getNames().length,
                cifar10Utils.getTotalOfBytes()+1);
        int k=0;
        for(int row=0; row<cifar10Utils.getNames().length; row++) {
            for(int col=0; col<cifar10Utils.getTotalOfBytes()+1;col++) {
                W.setEntry(row, col, doubles[k++] * 0.001);
            }
        }
        LOGGER.info(DuodecimoMatrixUtils.showRealMatrix("sampling weights", W, 6, 10));
        for(int i=0; i<100; i++) { // number of guesses
            Wtry = MatrixUtils.createRealMatrix(cifar10Utils.getNames().length,
                cifar10Utils.getTotalOfBytes()+1);
            k=0;
            for(int row=0; row<cifar10Utils.getNames().length; row++) {
                for(int col=0; col<cifar10Utils.getTotalOfBytes()+1;col++) {
                    W.setEntry(row, col, doubles[k++] * stepSize);
                }
            }
            Wtry = W.add(Wtry);
            if(sampleFirstWeights) {
                LOGGER.info(DuodecimoMatrixUtils.showRealMatrix("sampling try weights", Wtry, 6, 10));
                sampleFirstWeights = !sampleFirstWeights;
            }
            loss = (float) lossFunctionFullvectorized(XtrWithOnes, Y, Wtry);
            LOGGER.info("Loss (full vectorized calc) = ".concat(Double.toString(loss)));
            if(loss<bestloss) {
                bestloss = loss;
                BestW = Wtry.copy();
            }
            System.out.println(String.format("in guess attempt %d the loss was %f, "
                    + "best so far %f", i+1, loss, bestloss));
        }
        // BestW holds the weigths
        RealVector test;
        double groundLabel;
        RealVector scores;
        double predictLable;
        int accuracy = 0;
        int numberOfTestings = cifar10Utils.getTotalOfTests();
        for(k=0; k< numberOfTestings; k++) {
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
            System.out.println(String.format("test %d predicted = %f  ground = %f %c", 
                    k, predictLable, groundLabel, predictLable == groundLabel? '!' : ' '));
        }
        System.out.println(String.format("accuracy: %5.2f %%", 
                (float)(accuracy * 100 /numberOfTestings)));
    }

    /**
     * Compute the multiclass svm loss for a single example (x,y)
     * 
     * @param X a matrix with row = number of images and column = image bytes plus
     * extra ones column for bias trick
     * @param Y a vector with rows = number of images and one column with ground
     * truth image class
     * (e.g. between 0 and 9 in CIFAR-10)
     * @param W is the weight matrix (e.g. 10 x 3073 in CIFAR-10) with bias extra
     * column for the bias trick.
     * @return the loss as a double
     */
    public double lossFunctionUnvectorized(RealMatrix X, RealVector Y, RealMatrix W) {
        double loss = 0.0f;
        float delta = 1.0f;
        RealVector x1;
        int yGround;

        for (int lin = 0; lin < X.getRowDimension(); lin++) {
            x1 = X.getRowVector(lin);
            yGround = (int) Y.getEntry(lin);

            RealVector scores = W.operate(x1);
            double correctClassScore = scores.getEntry(yGround);
            int d = W.getRowDimension();
            // iterate over all wrong classes
            for (int i = 0; i < d; i++) {
                if (i == yGround) {
                    continue;
                }
                loss += Double.max(0, scores.getEntry(i)
                        - correctClassScore + delta);
            }
        }
        return loss;
    }

    /**
     * Compute the multiclass svm loss.
     * 
     * A faster half-vectorized implementation.
     * half-vectorized  refers to the fact that for a single example the 
     * implementation contains no for loops, but there is still one loop
     * over the examples (outside this function)
     * 
     * @param X a matrix with row = number of images and column = image bytes plus
     * extra ones column for bias trick
     * @param Y a vector with rows = number of images and one column with ground
     * truth image class
     * (e.g. between 0 and 9 in CIFAR-10)
     * @param W is the weight matrix (e.g. 10 x 3073 in CIFAR-10) with bias extra
     * column for the bias trick.
     * @return the loss as a double
     */
    public double lossFunctionSemivectorized(RealMatrix X, RealVector Y, RealMatrix W) {
        float loss = 0.0f;
        float delta = 1.0f;
        RealVector x1;
        RealVector scores;
        RealVector margins;
        int yGround;
        // iterate on all images
        for (int row = 0; row < X.getRowDimension(); row++) {
            // get x1 image vector from X matrix row
            x1 = X.getRowVector(row);
            yGround = (int) Y.getEntry(row);

            // scores becomes of size 10 x 1, the scores for each class
            scores = W.operate(x1);
            // compute the margins for all classes in one vector operation
            margins = scores.mapSubtract(scores.getEntry(yGround)).mapAdd(delta);
            // applies univariate Maximum function to self,
            // see Maximum class code below.
            margins.mapToSelf(new Maximum());
            // set the margin of the right class to 0.
            margins.setEntry(yGround, 0.0d);
            // dot product with a ones equal size vector
            // is the same as sum all entries.
            // accumulate this image loss.
            loss += margins.dotProduct(margins.map(new Ones()));
        }
        return loss;
    }

    /**
     * Compute the multiclass svm loss.
     * 
     * fastest full-vectorized implementation.
     * full-vectorized  refers to the fact that all is done without interation loops.
     * 
     * @param X a matrix with row = number of images and column = image bytes plus
     * extra ones column for bias trick
     * @param Y a vector with rows = number of images and one column with ground
     * truth image class
     * (e.g. between 0 and 9 in CIFAR-10)
     * @param W is the weight matrix (e.g. 10 x 3073 in CIFAR-10) with bias extra
     * column for the bias trick.
     * @return the loss as a double
     */
    public double lossFunctionFullvectorized(RealMatrix X, RealVector Y, RealMatrix W) {
        RealMatrix Scores = W.multiply(X.transpose()).transpose();
        RealVector Lvalues = new ArrayRealVector(Scores.getRowDimension());
        double delta = 1.0;
        Lvalues.mapToSelf(new LossUnivariateFunction(Scores, Y, delta));
        // L1Norm is the sum of the modules, but we can use it here
        // to get a simple sum: no loss is <0 because of max(0.0d, -) applyed,
        // so the modules won't change any value.
        double loss = Lvalues.getL1Norm();
        return loss;
    }

    private static class LossUnivariateFunction implements UnivariateFunction {
        double lin;
        RealMatrix Scores;
        RealVector Y;
        double delta;
        double loss;

        public LossUnivariateFunction(RealMatrix Scores, RealVector Y, double delta) {
            this.Scores = Scores;
            this.Y = Y;
            this.delta = delta;
            lin = 0.0d;
        }

        private final double lossOperation() {
            // matrix must be a vector num_images X num_classes
            // each entry contains sum(W, x).
            loss = 0.0d;
            for(double col=0.0d; col < Scores.getColumnDimension(); col++) {
                // visiting each class score
                if(col ==  Y.getEntry((int) lin)) {
                    // the correct class for the image
                    // do nothing
                } else {
                    loss += Double.max(0.0d, Scores.getEntry((int) lin, (int) col) - 
                    Scores.getEntry((int) lin, (int) Y.getEntry((int) (lin))) + delta);
                }
            }
            lin++;
            return loss;
        }

        @Override
        public double value(double x) {
            return lossOperation();
        }
        
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
    }
}
