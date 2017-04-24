
package sandbox;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import util.matrix.MatrixGrowDimension;

/**
 * We review in this code some matrices operations and the code to perform them.
 * 
 * There are the basic matrix operations, such as retrieving values, updating values,
 * basic mathematical operations like sum, multiplication.
 * We will also need at some point to grow some matrices dimensions, so the class 
 * MatrizGrowDimension was created in order to help.
 * 
 * A good resource in case you  are willing to review matriz basic theory and some 
 * operations is the Khan Academy online course on Matrices.
 * Here is the link: https://www.khanacademy.org/math/precalculus/precalc-matrices
 * 
 * Portuguese Version:
 * 
 * Existem algumas operações básicas com matrizes, como ler valores, alterar valores,
 * operaçoes matemáticas básicas como soma e multiplicação.
 * Vamos precisar, em algum momento, aumentar dimensões de matrizes, então a classe
 * MatrizGrowDimension foi criada para ajudar.
 * 
 * Um recurso interessante caso você deseje rever a teoria de matrizes e algumas
 * operações é o curso online Matrizes da Academia Khan.
 * O link em portugês é: https://pt.khanacademy.org/math/precalculus/precalc-matrices
 * 
 * @see util.matrix.MatrixGrowDimension
 * 
 * @author duo
 */
public class MatricesOperationsReview {

    public MatricesOperationsReview() {
        operationsSample();
    }

    final void operationsSample()  {
        System.out.println("Creating and operating RealMatrix.");

        // dimensionality (dimesnionalidade)

        System.out.println("Create some matrices, with several dimensionalities.");

        RealMatrix L1C1 = MatrixUtils.createRealMatrix(new double[][]{{1}});
        displayRealMatrix("lets build a matrix 1 x 1", 
                "RealMatrix L1C1 = MatrixUtils.createRealMatrix(new double[][]{{1}});", 
                "Matrix L1C1:",L1C1);

        RealMatrix L2C1 = MatrixUtils.createRealMatrix(new double[][]{{1},{2}});
        displayRealMatrix("lets build a matrix 2 x 1", 
                "RealMatrix L2C1 = MatrixUtils.createRealMatrix(new double[][]{{1},{2}});", 
                "Matrix L2C1:",L2C1);

        RealMatrix L1C2 = MatrixUtils.createRealMatrix(new double[][]{{1, 2}});
        displayRealMatrix("lets build a matrix 1 x 2", 
                "RealMatrix L1C2 = MatrixUtils.createRealMatrix(new double[][]{{1, 2}})", 
                "Matrix L1C2:",L1C2);

        RealMatrix L2C2 = MatrixUtils.createRealMatrix(new double[][]{{1,2},{3,4}});
        displayRealMatrix("lets build a matrix 2 x 2", 
                "RealMatrix L2C2 = MatrixUtils.createRealMatrix(new double[][]{{1,2},{3,4}})", 
                "Matrix L1C1:",L2C2);

        RealMatrix L2C4 = MatrixUtils.createRealMatrix(new double[][]{{1,2,3,4},{5,6,7,8}});
        displayRealMatrix("lets build a matrix 2 x 4", 
                "RealMatrix L2C4 = MatrixUtils.createRealMatrix(new double[][]{{1,2,3,4},{5,6,7,8}})", 
                "Matrix L2C4:",L2C4);

        RealMatrix L3C4 = MatrixGrowDimension.attachOnesRow(L2C4);
        displayRealMatrix("lets attach an extra row of ones to it", 
                "RealMatrix L3C4 = MatrixGrowDimension.attachOnesRow(L2C4)", 
                "Matrix L3C4:",L3C4);

        RealMatrix L2C5 = MatrixGrowDimension.attachOnesColumn(L2C4);
        displayRealMatrix("lets attach an extra column of ones instead", 
                "RealMatrix L2C5 = MatrixGrowDimension.attachOnesColumn(L2C4)", 
                "Matrix L2C5:",L2C5);

        L3C4 = MatrixGrowDimension.attachZerosRow(L2C4);
        displayRealMatrix("lets attach an extra row of zeros instead", 
                "L3C4 = MatrixGrowDimension.attachZerosRow(L2C4)", 
                "Matrix L3C4:",L3C4);

        L2C5 = MatrixGrowDimension.attachZerosColumn(L2C4);
        displayRealMatrix("lets attach an extra column of zeros instead", 
                "L2C5 = MatrixGrowDimension.attachZerosColumn(L2C4)", 
                "Matrix L2C5:",L2C5);

        // operations
        System.out.println("Some matrices operations.\n");
        System.out.println("Matrices multiplication.");
        
        RealMatrix A = MatrixUtils.createRealMatrix(new double[][]{{1,2,3}, {4,5,6}});
        displayRealMatrix("lets build a matrix", "Matrix A:",A);
        try {
            displayRealMatrix("let's try to multiply A to itself", 
                    "RealMatrix A = MatrixUtils.createRealMatrix(new double[][]{{1,2,3}, {4,5,6}})",
                    "Matrix A * A:", A.multiply(A));
        } catch (DimensionMismatchException dimensionMismatchException) {
            System.out.println("We can't multiply A *A, we get a dimension exception: " + 
                    dimensionMismatchException.getMessage());
            System.out.println("The rule is: left matrix number of columns "
                    + "must match right matrix number of lines!");
            System.out.println("A x A gives (2,3) * (2,3), does not match!");
            System.out.println("For example, we can multiply a (n, 2) matrix "
                    + "only by a (2, m) matrix!");
            System.out.println("\n");
        }
        displayRealMatrix("lets transpose it", "Matrix A':",A.transpose());
        displayRealMatrix("Now we can multiply both","Matrix A * A':",A.multiply(A.transpose()));
        
        System.out.println("Now we will try an operation that will happen when we "
                + "want perform Linear Classification.\nf(xi,W,b)=Wxi+b\n"
                + "We are multiplying a matrix to a vector (gives a vector)\n"
                + " and then adding to another vector, getting a vector");
        RealMatrix W = MatrixUtils.createRealMatrix(new double[][]{{0.2, -0.5, 0.1, 2.0},
            {1.5,1.3,2.1,0}, {0,0.25,0.2,-0.3}});
        displayRealMatrix("let's create a matrix W", 
                "RealMatrix W = MatrixUtils.createRealMatrix(new double[][]{{0.2, -0.5, 0.1, 2.0},\n" +
"            {1.5,1.3,2.1,0}, {0,0.25,0.2,-0.3}})",
                "Matrix W:",W);
        RealVector x = MatrixUtils.createRealVector(new double[]{56, 231, 24, 2});
        displayRealVector("let's create a vector x", 
                "RealVector x = MatrixUtils.createRealVector(new double[]{56, 231, 24, 2})", 
                "Vector x:", x);
        RealVector b = MatrixUtils.createRealVector(new double[]{1.1, 3.2, -1.2});
        displayRealVector("let's create a vector b", 
                "RealVector b = MatrixUtils.createRealVector(new double[]{1.1, 3.2, -1.2})", 
                "Vector b:", b);
        RealVector r = W.operate(x);
        displayRealVector("let's do W * x", 
                "RealVector R = W.operate(x)", 
                "Vector r:", r);
        r = r.add(b);
        displayRealVector("Finally let's do  W * x + b", 
                "r = r.add(b)", 
                "Vector r:", r);
}

    void displayRealMatrix(String title, String javaCode, String metadata, RealMatrix M) {
        System.out.println("task: " + title);
        System.out.println("Code: " + javaCode);
        displayRealMatrix(metadata, M);
    }

    void displayRealMatrix(String title, String metadata, RealMatrix M) {
        System.out.println(title);
        displayRealMatrix(metadata, M);
    }

    void displayRealMatrix(String metadata, RealMatrix M) {
        System.out.println(metadata + " (" + M.getRowDimension() + " X " + M.getColumnDimension() + ")");
        displayRealMatrix(M);
    }

    void displayRealMatrix(RealMatrix M) {
        for(int l=0; l< M.getRowDimension(); l++) {
            System.out.println("");
            for(int c = 0; c < M.getColumnDimension(); c++) {
                System.out.print(String.format(" %8.2f", M.getEntry(l, c)));
            }
        }
        System.out.println("\n");
    }

    void displayRealVector(String title, String javaCode, String metadata, RealVector v) {
        System.out.println("task: " + title);
        System.out.println("Code: " + javaCode);
        displayRealVector(metadata, v);
    }

    void displayRealVector(String title, String metadata, RealVector v) {
        System.out.println(title);
        displayRealVector(metadata, v);
    }

    void displayRealVector(String metadata, RealVector v) {
        System.out.println(metadata + " ( dimension = " + v.getDimension() + ")");
        displayRealVector(v);
    }

    void displayRealVector(RealVector v) {
        for(int l=0; l< v.getDimension(); l++) {
            System.out.println(String.format(" %8.2f", v.getEntry(l)));
        }
        System.out.println("\n");
    }

    public static void main(String [] args) {
        MatricesOperationsReview matricesOperationsReview = new MatricesOperationsReview();
    }
}
