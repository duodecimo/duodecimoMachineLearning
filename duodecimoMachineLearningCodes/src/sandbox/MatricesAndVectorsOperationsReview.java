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
package sandbox;

import static java.lang.Double.max;
import java.util.logging.Level;
import java.util.logging.Logger;
import ml.LinearPrediction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import util.matrix.Display;
import util.matrix.DuodecimoMatrixUtils;
import util.matrix.DuodecimoVectorUtils;

/**
 * We review in this code some matrices operations and the code to perform them.
 *
 * There are the basic matrix operations, such as retrieving values, updating
 * values, basic mathematical operations like sum, multiplication. We will also
 * need at some point to grow some matrices dimensions, so the class
 * MatrizGrowDimension was created in order to help.
 *
 * A good resource in case you are willing to review matriz basic theory and
 * some operations is the Khan Academy online course on Matrices. Here is the
 * link: https://www.khanacademy.org/math/precalculus/precalc-matrices
 *
 * Portuguese Version:
 *
 * Existem algumas operações básicas com matrizes, como ler valores, alterar
 * valores, operaçoes matemáticas básicas como soma e multiplicação. Vamos
 * precisar, em algum momento, aumentar dimensões de matrizes, então a classe
 * MatrizGrowDimension foi criada para ajudar.
 *
 * Um recurso interessante caso você deseje rever a teoria de matrizes e algumas
 * operações é o curso online Matrizes da Academia Khan. O link em portugês é:
 * https://pt.khanacademy.org/math/precalculus/precalc-matrices
 *
 * @see util.matrix.DuodecimoMatrixUtils
 *
 * @author duo
 */
public class MatricesAndVectorsOperationsReview {

    private static final Logger LOGGER = Logger.getGlobal();

    public MatricesAndVectorsOperationsReview() {
        //operationsSample();
        //showTest();
        // operationsForLinearClassification();
        //testTripleUnivariate();
        //fullVectorizedLossFunction();
        testLossFunctions();
    }

    final void testLossFunctions() {
        // create a matrix, X, hold images, each line an image
        // lines = 5 images, columns = 2 bytes per image
        RealMatrix X = MatrixUtils.createRealMatrix(new double[][]{
            {1.0, 2.0},
            {3.0, 4.0},
            {5.0, 6.0},
            {7.0, 8.0},
            {9.0, 10.0},
        });
        LOGGER.info(DuodecimoMatrixUtils.showRealMatrix("Create X (5 images of 2 bytes each):", X));

        // create a vector to hold each image in x classification.
        // lets say there are 4 classes, from 0 ~3.
        RealVector Y = new ArrayRealVector(new double[]
            {1.0, 3.0, 0.0, 2.0, 3.0}
        );
        LOGGER.info(DuodecimoVectorUtils.showRealVector("y: "
                + "(ground truth classification (3 classes) for each image)", Y));


        // create a matrix, W, holds the weights
        // with #categories=4 lines, #image bytes=2 columns
        RealMatrix W = MatrixUtils.createRealMatrix(new double[][]{
            {1.0d, 5.0d},
            {2.0d, 6.0d},
            {3.0d, 7.0d},
            {4.0d, 8.0d},
        });
        LOGGER.info(DuodecimoMatrixUtils.showRealMatrix("Create W (4 categories, 2 weight per image byte:", W));

        // create a vector, holds the bias
        RealVector bias = new ArrayRealVector(new double[]
            {1.0, 2.0, 3.0, 4.0}
        );
        LOGGER.info(DuodecimoVectorUtils.showRealVector("Create a vector to hold the bias:", bias));

        // bias trick: add bias vector as W new line, add a column of ones to X
        RealMatrix Wb = DuodecimoMatrixUtils.attachZerosColumn(W);
        Wb.setColumnVector(Wb.getColumnDimension()-1, bias);
        RealMatrix Xb = DuodecimoMatrixUtils.attachOnesColumn(X);
        LOGGER.info("Loss unvectorized: ".concat(Double.toString(lossFunctionUnvectorized(Xb, Y, Wb))));
        LOGGER.info("Loss semivectorized: ".concat(Double.toString(lossFunctionSemivectorized(Xb, Y, Wb))));
        LOGGER.info("Loss unvectorized: ".concat(Double.toString(lossFunctionFullvectorized(Xb, Y, Wb))));
    }

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

    public double lossFunctionSemivectorized(RealMatrix X, RealVector Y, RealMatrix W) {
        float loss = 0.0f;
        float delta = 1.0f;
        RealVector x1;
        int yGround;
        for (int lin = 0; lin < X.getRowDimension(); lin++) {
            x1 = X.getRowVector(lin);
            yGround = (int) Y.getEntry(lin);

            // scores becomes of size 10 x 1, the scores for each class
            RealVector scores = W.operate(x1);
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
            margins = scores.mapSubtract(scores.getEntry(yGround)).mapAdd(delta);
            margins.mapToSelf(new Maximum());
            margins.setEntry(yGround, 0.0d);
            loss += margins.dotProduct(margins.map(new Ones()));
        }
        return loss;
    }

    public double lossFunctionFullvectorized(RealMatrix X, RealVector Y, RealMatrix W) {
        RealMatrix Scores = W.multiply(X.transpose()).transpose();
        RealVector LV = new ArrayRealVector(Scores.getColumnDimension());
        double delta = 1.0;
        LV.mapToSelf(new LossUnivariateFunction(Scores, Y, delta));
        double loss = LV.getL1Norm();
        return loss;
    }

    private static class LossUnivariateFunction implements UnivariateFunction {
        double index;
        RealMatrix matrix;
        RealVector y;
        double delta;
        double loss;

        public LossUnivariateFunction(RealMatrix matrix, RealVector y, double delta) {
            this.matrix = matrix;
            this.y = y;
            this.delta = delta;
            index = 0.0d;
        }

        private final double lossOperation() {
            // matrix must be a vector num_images X num_classes
            // each entry contains sum(W, x).
            loss = 0.0d;
            for(double col=0.0d; col < matrix.getColumnDimension(); col++) {
                // visiting each class score
                if(col ==  y.getEntry((int) index)) {
                    // the correct class for the image
                    // do nothing
                } else {
                    loss += Double.max(0.0d, matrix.getEntry((int) index, (int) col) - 
                    matrix.getEntry((int) index, (int) y.getEntry((int) (index))) + delta);
                }
            }
            index++;
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


    final void fullVectorizedLossFunction() {
        LOGGER.setLevel(Level.INFO);
        LOGGER.info("Given the linear mapping function, f(xi, W, b) = Wxi+b\n"
                + "we can use matrix and vectors to perform the calculation.");

        // create a matrix, X, hold images, each line an image
        // lines = 5 images, columns = 2 bytes per image
        RealMatrix X = MatrixUtils.createRealMatrix(new double[][]{
            {1.0, 2.0},
            {3.0, 4.0},
            {5.0, 6.0},
            {7.0, 8.0},
            {9.0, 10.0},
        });
        LOGGER.info(DuodecimoMatrixUtils.showRealMatrix("Create X (5 images of 2 bytes each):", X));

        // create a vector to hold each image in x classification.
        // lets say there are 4 classes, from 0 ~3.
        RealVector y = new ArrayRealVector(new double[]
            {1.0, 3.0, 0.0, 2.0, 3.0}
        );
        LOGGER.info(DuodecimoVectorUtils.showRealVector("y: "
                + "(ground truth classification (3 classes) for each image)", y));


        // create a matrix, W, holds the weights
        // with #categories=4 lines, #image bytes=2 columns
        RealMatrix W = MatrixUtils.createRealMatrix(new double[][]{
            {1.0d, 5.0d},
            {2.0d, 6.0d},
            {3.0d, 7.0d},
            {4.0d, 8.0d},
        });
        LOGGER.info(DuodecimoMatrixUtils.showRealMatrix("Create W (4 categories, 2 weight per image byte:", W));

        // create a vector, holds the bias
        RealVector bias = new ArrayRealVector(new double[]
            {1.0, 2.0, 3.0, 4.0}
        );
        LOGGER.info(DuodecimoVectorUtils.showRealVector("Create a vector to hold the bias:", bias));

        // bias trick: add bias vector as W new line, add a column of ones to X
        RealMatrix Wb = DuodecimoMatrixUtils.attachZerosColumn(W);
        Wb.setColumnVector(Wb.getColumnDimension()-1, bias);
        RealMatrix Xb = DuodecimoMatrixUtils.attachOnesColumn(X);

        RealVector ones = new ArrayRealVector(Xb.getRowDimension());
        ones.set(1.0d);
        
        LOGGER.info(DuodecimoMatrixUtils.showRealMatrix("Wb:", Wb) +
                DuodecimoMatrixUtils.showRealMatrix("Xb:", Xb) +
                DuodecimoVectorUtils.showRealVector("ones: ", ones));

        LOGGER.info("To make all in one matrices operation: " + 
                DuodecimoMatrixUtils.showRealMatrix("Wb:", Wb) +
                DuodecimoMatrixUtils.showRealMatrix("Xb:", Xb) +
                DuodecimoMatrixUtils.showRealMatrix("Wb x Xb'", Wb.multiply(Xb.transpose())));
        // Got a matrix where each line is a wi x xi + b to all the bytes of a image.
        // Or, a categories by num_images matrix.
        // Now I have to subtract Yi from each column.
        // Then, add delta.
        // Then, sum (excluding the Yi itself line) into a vector of distances.
        // *** Humm, maybe a univariate, with an if? ********
        // yes, I build the result vector, and apply the univariate to it, pass
        // the matriz and Y to it, and build the calculus. the overriden value just
        // call the calculus!
        // then floor to 0. (part of the calculus)!
        // then sum the vector, getting the error value.
        RealMatrix Scores = Wb.multiply(Xb.transpose()).transpose();
        LOGGER.info(DuodecimoMatrixUtils.showRealMatrix("Scores: ", Scores));
        RealVector LV = new ArrayRealVector(Scores.getColumnDimension());
        double delta = 10.0;
        LV.mapToSelf(new LossUnivariateFunction(Scores, y, delta));
        LOGGER.info(DuodecimoVectorUtils.showRealVector("LV: ", LV));
        double loss = LV.getL1Norm();
        LOGGER.info("Loss = " + loss);
    }

    /**
     * This inner class implements the interface
     * org.apache.commons.math3.analysis.UnivariateFunction
     * 
     * By doing so, it can becomes an argument to RealVector.mapToSelf().
     * mapToSelf will change all vector entries by applying value() to them.
     * In this particular implementation it will triple all entries.
     */
    private static class TripleUnivariateFunction implements UnivariateFunction {

        @Override
        public double value(double x) {
            return 3 * x;
        }
    }

    final void testTripleUnivariate() {
        // define a vector
        RealVector realVector = new ArrayRealVector(new double[]{1.0d, 2.0d, 3.0d});
        LOGGER.info(DuodecimoVectorUtils.showRealVector("Define a vector", realVector));
        realVector.mapToSelf(new TripleUnivariateFunction());
        LOGGER.info(DuodecimoVectorUtils.showRealVector("vector after operation", realVector));
    }
    final void operationsForLinearClassification() {
        LOGGER.setLevel(Level.INFO);
        LOGGER.info("Given the linear mapping function, f(xi, W, b) = Wxi+b\n"
                + "we can use matrix and vectors to perform the calculation.");
        // create a vector, holds the bias
        /*
        RealVector bias = new ArrayRealVector(new double[]
        {1.1, 3.2, -1.2}
        );
        */
        RealVector bias = new ArrayRealVector(new double[]
            {1.0, 2.0, 3.0}
        );

        LOGGER.info(DuodecimoVectorUtils.showRealVector("Create a vector to hold the bias:", bias));
        // create a matrix, W, holds the weights
        // each column holds the weight for a line in X
        /*
        RealMatrix W = MatrixUtils.createRealMatrix(new double[][]{
        {0.2, -0.5, -0.1, 2.0},
        {1.5, 1.3, 2.1, 0.0},
        {0.0, 0.25, 0.2, -0.3}
        });
        */
        RealMatrix W = MatrixUtils.createRealMatrix(new double[][]{
            {1.0d, 2.0d, 3.0d, 4.0d},
            {5.0d, 6.0d, 7.0d, 8.0d},
            {9.0d, 10.0d, 11.0d, 12.0d}
        });
        LOGGER.info(DuodecimoMatrixUtils.showRealMatrix("Create W:", W));
        // create a matrix, X, hold images, each line an image
        /*
        RealMatrix X = MatrixUtils.createRealMatrix(new double[][]{
        {56, 231, 24, 2},
        {10, 10, 10, 10}
        });
        */
        RealMatrix X = MatrixUtils.createRealMatrix(new double[][]{
            {1.0, 2.0, 3.0},
            {4.0, 5.0, 6.0},
        });
        /*
        LOGGER.info(DuodecimoMatrixUtils.showRealMatrix("Create X:", X));

        LOGGER.info("Calculate f(xi, W, bias) = Wx1+b for the first image on X)");

        RealMatrix X1 = X.getSubMatrix(0, 0, 0, 3).transpose();
        LOGGER.info(DuodecimoMatrixUtils.showRealMatrix("X1:", X1));
        
        RealMatrix WX1 = W.multiply(X1);
        LOGGER.info(DuodecimoMatrixUtils.showRealMatrix("Wx1:", WX1));
        
        LOGGER.info(DuodecimoMatrixUtils.showRealMatrix("Compare com WX': ", W.multiply(X.transpose())));
        
        LOGGER.info(DuodecimoVectorUtils.showRealVector("WX1 + b: ", WX1.getColumnVector(0).add(bias)));
        */

        RealVector ones = new ArrayRealVector(X.getRowDimension());
        ones.set(1.0d);

        LOGGER.info("To make all in one matrices operation: " + 
                DuodecimoMatrixUtils.showRealMatrix("W:", W) +
                DuodecimoMatrixUtils.showRealMatrix("X:", X) +
                DuodecimoMatrixUtils.showRealMatrix("W' x X'", W.transpose().multiply(X.transpose())) +
                DuodecimoVectorUtils.showRealVector("W' x X' x ones", (W.transpose().multiply(X.transpose())).operate(ones)));
        // Got a matrix where each line is a wi x xi + b to all the bytes of a image.
    }

    final void showTest() {
        // create a matrix
        RealMatrix realMatrix = MatrixUtils.createRealMatrix(new double[][]{
            {1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0},
            {11.0, 12.0, 13.0, 14.0, 15.0, 16.0, 17.0, 18.0, 19.0, 20.0},
            {21.0, 22.0, 23.0, 24.0, 25.0, 26.0, 27.0, 28.0, 29.0, 30.0},
            {31.0, 32.0, 33.0, 34.0, 35.0, 36.0, 37.0, 38.0, 39.0, 40.0},
            {41.0, 42.0, 43.0, 44.0, 45.0, 46.0, 47.0, 48.0, 49.0, 50.0},
            {51.0, 52.0, 53.0, 54.0, 55.0, 56.0, 57.0, 58.0, 59.0, 60.0},
            {61.0, 62.0, 63.0, 64.0, 65.0, 66.0, 67.0, 68.0, 69.0, 70.0}
        });
        // show entire matrix
        LOGGER.info(DuodecimoMatrixUtils.showRealMatrix("Toda matriz", realMatrix));
        LOGGER.info(DuodecimoMatrixUtils.showRealMatrix("matriz truncada em (3,7)", realMatrix, 3, 7));
    }

    final void operationsSample() {
        LOGGER.info(java.util.ResourceBundle.getBundle("sandbox/Bundle").getString("CREATING AND OPERATING REALMATRIX."));
        LOGGER.info("");

        // dimensionality (dimesnionalidade)
        LOGGER.info(java.util.ResourceBundle.getBundle("sandbox/Bundle").getString("CREATE SOME MATRICES, WITH SEVERAL DIMENSIONALITIES."));
        LOGGER.info("");

        RealMatrix L1C1 = MatrixUtils.createRealMatrix(new double[][]{{1}});
        displayRealMatrix(java.util.ResourceBundle.getBundle("sandbox/Bundle").getString("LETS BUILD A MATRIX 1 X 1"),
                java.util.ResourceBundle.getBundle("sandbox/Bundle").getString("REALMATRIX L1C1 = MATRIXUTILS.CREATEREALMATRIX(NEW DOUBLE[][]{{1}});"),
                java.util.ResourceBundle.getBundle("sandbox/Bundle").getString("MATRIX L1C1:"), L1C1);

        RealMatrix L2C1 = MatrixUtils.createRealMatrix(new double[][]{{1}, {2}});
        displayRealMatrix(java.util.ResourceBundle.getBundle("sandbox/Bundle").getString("LETS BUILD A MATRIX 2 X 1"),
                java.util.ResourceBundle.getBundle("sandbox/Bundle").getString("REALMATRIX L2C1 = MATRIXUTILS.CREATEREALMATRIX(NEW DOUBLE[][]{{1},{2}});"),
                java.util.ResourceBundle.getBundle("sandbox/Bundle").getString("MATRIX L2C1:"), L2C1);

        RealMatrix L1C2 = MatrixUtils.createRealMatrix(new double[][]{{1, 2}});
        displayRealMatrix(java.util.ResourceBundle.getBundle("sandbox/Bundle").getString("LETS BUILD A MATRIX 1 X 2"),
                java.util.ResourceBundle.getBundle("sandbox/Bundle").getString("REALMATRIX L1C2 = MATRIXUTILS.CREATEREALMATRIX(NEW DOUBLE[][]{{1, 2}})"),
                java.util.ResourceBundle.getBundle("sandbox/Bundle").getString("MATRIX L1C2:"), L1C2);

        RealMatrix L2C2 = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {3, 4}});
        displayRealMatrix(java.util.ResourceBundle.getBundle("sandbox/Bundle").getString("LETS BUILD A MATRIX 2 X 2"),
                java.util.ResourceBundle.getBundle("sandbox/Bundle").getString("REALMATRIX L2C2 = MATRIXUTILS.CREATEREALMATRIX(NEW DOUBLE[][]{{1,2},{3,4}})"),
                java.util.ResourceBundle.getBundle("sandbox/Bundle").getString("MATRIX L1C1:"), L2C2);

        RealMatrix L2C4 = MatrixUtils.createRealMatrix(new double[][]{{1, 2, 3, 4}, {5, 6, 7, 8}});
        displayRealMatrix(java.util.ResourceBundle.getBundle("sandbox/Bundle").getString("LETS BUILD A MATRIX 2 X 4"),
                java.util.ResourceBundle.getBundle("sandbox/Bundle").getString("REALMATRIX L2C4 = MATRIXUTILS.CREATEREALMATRIX(NEW DOUBLE[][]{{1,2,3,4},{5,6,7,8}})"),
                java.util.ResourceBundle.getBundle("sandbox/Bundle").getString("MATRIX L2C4:"), L2C4);

        RealMatrix L3C4 = DuodecimoMatrixUtils.attachOnesRow(L2C4);
        displayRealMatrix(java.util.ResourceBundle.getBundle("sandbox/Bundle").getString("LETS ATTACH AN EXTRA ROW OF ONES TO IT"),
                java.util.ResourceBundle.getBundle("sandbox/Bundle").getString("REALMATRIX L3C4 = MATRIXGROWDIMENSION.ATTACHONESROW(L2C4)"),
                java.util.ResourceBundle.getBundle("sandbox/Bundle").getString("MATRIX L3C4:"), L3C4);

        RealMatrix L2C5 = DuodecimoMatrixUtils.attachOnesColumn(L2C4);
        displayRealMatrix(java.util.ResourceBundle.getBundle("sandbox/Bundle").getString("LETS ATTACH AN EXTRA COLUMN OF ONES INSTEAD"),
                java.util.ResourceBundle.getBundle("sandbox/Bundle").getString("REALMATRIX L2C5 = MATRIXGROWDIMENSION.ATTACHONESCOLUMN(L2C4)"),
                java.util.ResourceBundle.getBundle("sandbox/Bundle").getString("MATRIX L2C5:"), L2C5);

        L3C4 = DuodecimoMatrixUtils.attachZerosRow(L2C4);
        displayRealMatrix(java.util.ResourceBundle.getBundle("sandbox/Bundle").getString("LETS ATTACH AN EXTRA ROW OF ZEROS INSTEAD"),
                java.util.ResourceBundle.getBundle("sandbox/Bundle").getString("L3C4 = MATRIXGROWDIMENSION.ATTACHZEROSROW(L2C4)"),
                java.util.ResourceBundle.getBundle("sandbox/Bundle").getString("MATRIX L3C4:"), L3C4);

        L2C5 = DuodecimoMatrixUtils.attachZerosColumn(L2C4);
        displayRealMatrix(java.util.ResourceBundle.getBundle("sandbox/Bundle").getString("LETS ATTACH AN EXTRA COLUMN OF ZEROS INSTEAD"),
                java.util.ResourceBundle.getBundle("sandbox/Bundle").getString("L2C5 = MATRIXGROWDIMENSION.ATTACHZEROSCOLUMN(L2C4)"),
                java.util.ResourceBundle.getBundle("sandbox/Bundle").getString("MATRIX L2C5:"), L2C5);

        // operations
        LOGGER.info(java.util.ResourceBundle.getBundle("sandbox/Bundle").getString("SOME MATRICES OPERATIONS.\\NEWLINE"));
        LOGGER.info(java.util.ResourceBundle.getBundle("sandbox/Bundle").getString("MATRICES MULTIPLICATION."));

        RealMatrix A = MatrixUtils.createRealMatrix(new double[][]{{1, 2, 3}, {4, 5, 6}});
        displayRealMatrix(java.util.ResourceBundle.getBundle("sandbox/Bundle").getString("LETS BUILD A MATRIX"), java.util.ResourceBundle.getBundle("sandbox/Bundle").getString("MATRIX A:"), A);
        try {
            displayRealMatrix(java.util.ResourceBundle.getBundle("sandbox/Bundle").getString("LET'S TRY TO MULTIPLY A TO ITSELF"),
                    java.util.ResourceBundle.getBundle("sandbox/Bundle").getString("REALMATRIX A = MATRIXUTILS.CREATEREALMATRIX(NEW DOUBLE[][]{{1,2,3}, {4,5,6}})"),
                    java.util.ResourceBundle.getBundle("sandbox/Bundle").getString("MATRIX A * A:"), A.multiply(A));
        } catch (DimensionMismatchException dimensionMismatchException) {
            LOGGER.info(java.util.ResourceBundle.getBundle("sandbox/Bundle").getString("WE CAN'T MULTIPLY A *A, WE GET A DIMENSION EXCEPTION: ")
                    + dimensionMismatchException.getMessage());
            LOGGER.info(java.util.ResourceBundle.getBundle("sandbox/Bundle").getString("THE RULE IS: LEFT MATRIX NUMBER OF COLUMNS ")
                    + java.util.ResourceBundle.getBundle("sandbox/Bundle").getString("MUST MATCH RIGHT MATRIX NUMBER OF LINES!"));
            LOGGER.info(java.util.ResourceBundle.getBundle("sandbox/Bundle").getString("A X A GIVES (2,3) * (2,3), DOES NOT MATCH!"));
            LOGGER.info(java.util.ResourceBundle.getBundle("sandbox/Bundle").getString("FOR EXAMPLE, WE CAN MULTIPLY A (N, 2) MATRIX ")
                    + java.util.ResourceBundle.getBundle("sandbox/Bundle").getString("ONLY BY A (2, M) MATRIX!"));
            LOGGER.info(java.util.ResourceBundle.getBundle("sandbox/Bundle").getString("\\NEWLINE"));
        }
        displayRealMatrix(java.util.ResourceBundle.getBundle("sandbox/Bundle").getString("LETS TRANSPOSE IT"), java.util.ResourceBundle.getBundle("sandbox/Bundle").getString("MATRIX A':"), A.transpose());
        displayRealMatrix(java.util.ResourceBundle.getBundle("sandbox/Bundle").getString("NOW WE CAN MULTIPLY BOTH"), java.util.ResourceBundle.getBundle("sandbox/Bundle").getString("MATRIX A * A':"), A.multiply(A.transpose()));

        LOGGER.info(java.util.ResourceBundle.getBundle("sandbox/Bundle").getString("NOW WE WILL TRY AN OPERATION THAT WILL HAPPEN WHEN WE ").concat(
                java.util.ResourceBundle.getBundle("sandbox/Bundle").getString("WANT PERFORM LINEAR CLASSIFICATION.\\NEWLINE F(XI,W,B)=WXI+B\\NEWLINE")).concat(
                java.util.ResourceBundle.getBundle("sandbox/Bundle").getString("WE ARE MULTIPLYING A MATRIX TO A VECTOR (GIVES A VECTOR)\\NEWLINE").concat(
                java.util.ResourceBundle.getBundle("sandbox/Bundle").getString(" AND THEN ADDING TO ANOTHER VECTOR, GETTING A VECTOR"))));
        RealMatrix W = MatrixUtils.createRealMatrix(new double[][]{{0.2, -0.5, 0.1, 2.0},
        {1.5, 1.3, 2.1, 0}, {0, 0.25, 0.2, -0.3}});
        displayRealMatrix(java.util.ResourceBundle.getBundle("sandbox/Bundle").getString("LET'S CREATE A MATRIX W"),
                java.util.ResourceBundle.getBundle("sandbox/Bundle").getString("REALMATRIX W = MATRIXUTILS.CREATEREALMATRIX(NEW DOUBLE[][]{{0.2, -0.5, 0.1, 2.0},\\NEWLINE")
                + java.util.ResourceBundle.getBundle("sandbox/Bundle").getString("            {1.5,1.3,2.1,0}, {0,0.25,0.2,-0.3}})"),
                java.util.ResourceBundle.getBundle("sandbox/Bundle").getString("MATRIX W:"), W);
        RealVector x = MatrixUtils.createRealVector(new double[]{56, 231, 24, 2});
        displayRealVector(java.util.ResourceBundle.getBundle("sandbox/Bundle").getString("LET'S CREATE A VECTOR X"),
                java.util.ResourceBundle.getBundle("sandbox/Bundle").getString("REALVECTOR X = MATRIXUTILS.CREATEREALVECTOR(NEW DOUBLE[]{56, 231, 24, 2})"),
                java.util.ResourceBundle.getBundle("sandbox/Bundle").getString("VECTOR X:"), x);
        RealVector b = MatrixUtils.createRealVector(new double[]{1.1, 3.2, -1.2});
        displayRealVector(java.util.ResourceBundle.getBundle("sandbox/Bundle").getString("LET'S CREATE A VECTOR B"),
                java.util.ResourceBundle.getBundle("sandbox/Bundle").getString("REALVECTOR B = MATRIXUTILS.CREATEREALVECTOR(NEW DOUBLE[]{1.1, 3.2, -1.2})"),
                java.util.ResourceBundle.getBundle("sandbox/Bundle").getString("VECTOR B:"), b);
        RealVector r = W.operate(x);
        displayRealVector(java.util.ResourceBundle.getBundle("sandbox/Bundle").getString("LET'S DO W * X"),
                java.util.ResourceBundle.getBundle("sandbox/Bundle").getString("REALVECTOR R = W.OPERATE(X)"),
                java.util.ResourceBundle.getBundle("sandbox/Bundle").getString("VECTOR R:"), r);
        r = r.add(b);
        displayRealVector(java.util.ResourceBundle.getBundle("sandbox/Bundle").getString("FINALLY LET'S DO  W * X + B"),
                java.util.ResourceBundle.getBundle("sandbox/Bundle").getString("R = R.ADD(B)"),
                java.util.ResourceBundle.getBundle("sandbox/Bundle").getString("VECTOR R:"), r);
    }

    void VectorNormalization() throws Exception {
        // vector normalization
        RealVector realVector = new ArrayRealVector(new double[]{128, 44, 199, 88, 255, 99, 31, 0, 127, 75, 164});
        Display.displayRealVector("Antes de normalizar:", realVector);
        realVector = DuodecimoVectorUtils.normalizeAndScaleToRange(realVector, new double[]{-1,1});
        Display.displayRealVector("Após normalizar:", realVector);
    }

    void displayRealMatrix(String title, String javaCode, String metadata, RealMatrix M) {
        LOGGER.info(java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("sandbox/Bundle").getString("TASK: {0}"), new Object[]{title}));
        LOGGER.info(java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("sandbox/Bundle").getString("CODE: {0}"), new Object[]{javaCode}));
        displayRealMatrix(metadata, M);
    }

    void displayRealMatrix(String title, String metadata, RealMatrix M) {
        LOGGER.info(title);
        displayRealMatrix(metadata, M);
    }

    void displayRealMatrix(String metadata, RealMatrix M) {
        LOGGER.info(metadata + java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("sandbox/Bundle").getString(" ({0} X {1})"), new Object[]{M.getRowDimension(), M.getColumnDimension()}));
        displayRealMatrix(M);
    }

    void displayRealMatrix(RealMatrix M) {
        for (int l = 0; l < M.getRowDimension(); l++) {
            LOGGER.info("");
            for (int c = 0; c < M.getColumnDimension(); c++) {
                System.out.print(String.format(java.util.ResourceBundle.getBundle("sandbox/Bundle").getString(" %8.2F"), M.getEntry(l, c)));
            }
        }
        LOGGER.info(java.util.ResourceBundle.getBundle("sandbox/Bundle").getString("\\NEWLINE"));
    }

    void displayRealVector(String title, String javaCode, String metadata, RealVector v) {
        LOGGER.info(java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("sandbox/Bundle").getString("TASK: {0}"), new Object[]{title}));
        LOGGER.info(java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("sandbox/Bundle").getString("CODE: {0}"), new Object[]{javaCode}));
        displayRealVector(metadata, v);
    }

    void displayRealVector(String title, String metadata, RealVector v) {
        LOGGER.info(title);
        displayRealVector(metadata, v);
    }

    void displayRealVector(String metadata, RealVector v) {
        LOGGER.info(metadata + java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("sandbox/Bundle").getString(" ( DIMENSION = {0})"), new Object[]{v.getDimension()}));
        displayRealVector(v);
    }

    void displayRealVector(RealVector v) {
        for (int l = 0; l < v.getDimension(); l++) {
            LOGGER.info(String.format(java.util.ResourceBundle.getBundle("sandbox/Bundle").getString(" %8.2F"), v.getEntry(l)));
        }
        LOGGER.info(java.util.ResourceBundle.getBundle("sandbox/Bundle").getString("\\NEWLINE"));
    }

    public static void main(String[] args) {
        MatricesAndVectorsOperationsReview matricesOperationsReview = new MatricesAndVectorsOperationsReview();
    }
}
