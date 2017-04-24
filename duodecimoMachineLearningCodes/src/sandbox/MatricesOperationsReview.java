
package sandbox;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

/**
 *
 * @author duo
 */
public class MatricesOperationsReview {

    public MatricesOperationsReview() {
        operationsSample();
    }

    final void operationsSample()  {
        // dimensionality
        System.out.println("Some matrices with several dimensionalities.");
        RealMatrix L1C1 = new Array2DRowRealMatrix(new double[]{1});
        displayMatrix("lets build a matrix 1 x 1", "Matrix L1C1:",L1C1);
        RealMatrix L2C1 = new Array2DRowRealMatrix(new double[][]{{1},{2}});
        displayMatrix("lets build a matrix 2 x 1", "Matrix L2C1:",L2C1);
        RealMatrix L1C2 = new Array2DRowRealMatrix(new double[][]{{1, 2}});
        displayMatrix("lets build a matrix 1 x 2", "Matrix L1C2:",L1C2);
        RealMatrix L2C2 = new Array2DRowRealMatrix(new double[][]{{1,2},{3,4}});
        displayMatrix("lets build a matrix 2 x 2", "Matrix L1C1:",L2C2);
        // operations
        System.out.println("Some matrices operations.");
        RealMatrix A = new Array2DRowRealMatrix(new double[][]{{1,2,3}, {4,5,6}});
        displayMatrix("lets build a matrix", "Matrix A:",A);
        try {
            displayMatrix("let's multiply A to A", "Matrix A * A:", A.multiply(A));
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
        displayMatrix("lets trasnpose it", "Matrix A':",A.transpose());
        displayMatrix("Now we can multiply both","Matrix A * A':",A.multiply(A.transpose()));

        /*
        RealMatrix W = new Array2DRowRealMatrix(new double[][]{{0.2, -0.5, 0.1, 2.0},
            {1.5,1.3,2.1,0}, {0,0.25,0.2,-0.3}});
        System.out.println("Display W:");
        displayMatrix(W);
        RealVector x = new ArrayRealVector(new double[]{56, 231, 24, 2});
        System.out.println("Display x:");
        displayVector(x);
        System.out.println("Display b:");
        RealVector b = new ArrayRealVector(new double[]{1.1, 3.2, -1.2});
        displayVector(b);
        RealMatrix R = W.multiply(new Array2DRowRealMatrix(x.toArray()));
        System.out.println("Display W * x");
        displayMatrix(R);
        System.out.println("Display W * x + b");
        displayMatrix(R.add(new Array2DRowRealMatrix(b.toArray())));
        */
}

    void displayMatrix(String title, String metadata, RealMatrix M) {
        System.out.println(title);
        displayMatrix(metadata, M);
    }

    void displayMatrix(String metadata, RealMatrix M) {
        System.out.println(metadata + " (" + M.getRowDimension() + " X " + M.getColumnDimension() + ")");
        displayMatrix(M);
    }

    void displayMatrix(RealMatrix M) {
        for(int l=0; l< M.getRowDimension(); l++) {
            System.out.println("");
            for(int c = 0; c < M.getColumnDimension(); c++) {
                System.out.print(String.format(" %8.2f", M.getEntry(l, c)));
            }
        }
        System.out.println("\n");
    }

    void displayVector(RealVector v) {
        for(int l=0; l< v.getDimension(); l++) {
            System.out.print(String.format(" %8.2f", v.getEntry(l)));
        }
        System.out.println("\n");
    }

    public static void main(String [] args) {
        MatricesOperationsReview matricesOperationsReview = new MatricesOperationsReview();
    }
}
