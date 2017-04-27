package sandbox;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import util.matrix.DuodecimoMatrixUtils;

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
public class MatricesOperationsReview {

    public MatricesOperationsReview() {
        //operationsSample();
        showTest();
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
        System.out.println("Toda matriz");
        DuodecimoMatrixUtils.showRealMatrix(realMatrix, -1, -1);
        System.out.println("matriz truncada em (3,7)");
        DuodecimoMatrixUtils.showRealMatrix(realMatrix, 3, 7);
    }

    final void operationsSample() {
        System.out.println(java.util.ResourceBundle.getBundle("sandbox/Bundle").getString("CREATING AND OPERATING REALMATRIX."));
        System.out.println("");

        // dimensionality (dimesnionalidade)
        System.out.println(java.util.ResourceBundle.getBundle("sandbox/Bundle").getString("CREATE SOME MATRICES, WITH SEVERAL DIMENSIONALITIES."));
        System.out.println("");

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
        System.out.println(java.util.ResourceBundle.getBundle("sandbox/Bundle").getString("SOME MATRICES OPERATIONS.\\NEWLINE"));
        System.out.println(java.util.ResourceBundle.getBundle("sandbox/Bundle").getString("MATRICES MULTIPLICATION."));

        RealMatrix A = MatrixUtils.createRealMatrix(new double[][]{{1, 2, 3}, {4, 5, 6}});
        displayRealMatrix(java.util.ResourceBundle.getBundle("sandbox/Bundle").getString("LETS BUILD A MATRIX"), java.util.ResourceBundle.getBundle("sandbox/Bundle").getString("MATRIX A:"), A);
        try {
            displayRealMatrix(java.util.ResourceBundle.getBundle("sandbox/Bundle").getString("LET'S TRY TO MULTIPLY A TO ITSELF"),
                    java.util.ResourceBundle.getBundle("sandbox/Bundle").getString("REALMATRIX A = MATRIXUTILS.CREATEREALMATRIX(NEW DOUBLE[][]{{1,2,3}, {4,5,6}})"),
                    java.util.ResourceBundle.getBundle("sandbox/Bundle").getString("MATRIX A * A:"), A.multiply(A));
        } catch (DimensionMismatchException dimensionMismatchException) {
            System.out.println(java.util.ResourceBundle.getBundle("sandbox/Bundle").getString("WE CAN'T MULTIPLY A *A, WE GET A DIMENSION EXCEPTION: ")
                    + dimensionMismatchException.getMessage());
            System.out.println(java.util.ResourceBundle.getBundle("sandbox/Bundle").getString("THE RULE IS: LEFT MATRIX NUMBER OF COLUMNS ")
                    + java.util.ResourceBundle.getBundle("sandbox/Bundle").getString("MUST MATCH RIGHT MATRIX NUMBER OF LINES!"));
            System.out.println(java.util.ResourceBundle.getBundle("sandbox/Bundle").getString("A X A GIVES (2,3) * (2,3), DOES NOT MATCH!"));
            System.out.println(java.util.ResourceBundle.getBundle("sandbox/Bundle").getString("FOR EXAMPLE, WE CAN MULTIPLY A (N, 2) MATRIX ")
                    + java.util.ResourceBundle.getBundle("sandbox/Bundle").getString("ONLY BY A (2, M) MATRIX!"));
            System.out.println(java.util.ResourceBundle.getBundle("sandbox/Bundle").getString("\\NEWLINE"));
        }
        displayRealMatrix(java.util.ResourceBundle.getBundle("sandbox/Bundle").getString("LETS TRANSPOSE IT"), java.util.ResourceBundle.getBundle("sandbox/Bundle").getString("MATRIX A':"), A.transpose());
        displayRealMatrix(java.util.ResourceBundle.getBundle("sandbox/Bundle").getString("NOW WE CAN MULTIPLY BOTH"), java.util.ResourceBundle.getBundle("sandbox/Bundle").getString("MATRIX A * A':"), A.multiply(A.transpose()));

        System.out.println(java.util.ResourceBundle.getBundle("sandbox/Bundle").getString("NOW WE WILL TRY AN OPERATION THAT WILL HAPPEN WHEN WE ").concat(
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

    void displayRealMatrix(String title, String javaCode, String metadata, RealMatrix M) {
        System.out.println(java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("sandbox/Bundle").getString("TASK: {0}"), new Object[]{title}));
        System.out.println(java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("sandbox/Bundle").getString("CODE: {0}"), new Object[]{javaCode}));
        displayRealMatrix(metadata, M);
    }

    void displayRealMatrix(String title, String metadata, RealMatrix M) {
        System.out.println(title);
        displayRealMatrix(metadata, M);
    }

    void displayRealMatrix(String metadata, RealMatrix M) {
        System.out.println(metadata + java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("sandbox/Bundle").getString(" ({0} X {1})"), new Object[]{M.getRowDimension(), M.getColumnDimension()}));
        displayRealMatrix(M);
    }

    void displayRealMatrix(RealMatrix M) {
        for (int l = 0; l < M.getRowDimension(); l++) {
            System.out.println("");
            for (int c = 0; c < M.getColumnDimension(); c++) {
                System.out.print(String.format(java.util.ResourceBundle.getBundle("sandbox/Bundle").getString(" %8.2F"), M.getEntry(l, c)));
            }
        }
        System.out.println(java.util.ResourceBundle.getBundle("sandbox/Bundle").getString("\\NEWLINE"));
    }

    void displayRealVector(String title, String javaCode, String metadata, RealVector v) {
        System.out.println(java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("sandbox/Bundle").getString("TASK: {0}"), new Object[]{title}));
        System.out.println(java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("sandbox/Bundle").getString("CODE: {0}"), new Object[]{javaCode}));
        displayRealVector(metadata, v);
    }

    void displayRealVector(String title, String metadata, RealVector v) {
        System.out.println(title);
        displayRealVector(metadata, v);
    }

    void displayRealVector(String metadata, RealVector v) {
        System.out.println(metadata + java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("sandbox/Bundle").getString(" ( DIMENSION = {0})"), new Object[]{v.getDimension()}));
        displayRealVector(v);
    }

    void displayRealVector(RealVector v) {
        for (int l = 0; l < v.getDimension(); l++) {
            System.out.println(String.format(java.util.ResourceBundle.getBundle("sandbox/Bundle").getString(" %8.2F"), v.getEntry(l)));
        }
        System.out.println(java.util.ResourceBundle.getBundle("sandbox/Bundle").getString("\\NEWLINE"));
    }

    public static void main(String[] args) {
        MatricesOperationsReview matricesOperationsReview = new MatricesOperationsReview();
    }
}
