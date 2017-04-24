
package sandbox;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.MatrixUtils;
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
        System.out.println("Creating and operating RealMatrix.");
        // dimensionality
        System.out.println("Some matrices with several dimensionalities.");
        RealMatrix L1C1 = MatrixUtils.createRealMatrix(new double[][]{{1}});
        MatricesOperationsReview.this.displayRealMatrix("lets build a matrix 1 x 1", 
                "RealMatrix L1C1 = MatrixUtils.createRealMatrix(new double[]{{1}})", "Matrix L1C1:",L1C1);
        RealMatrix L2C1 = MatrixUtils.createRealMatrix(new double[][]{{1},{2}});
        MatricesOperationsReview.this.displayRealMatrix("lets build a matrix 2 x 1", 
                "RealMatrix L2C1 = MatrixUtils.createRealMatrix(new double[][]{{1},{2}})" ,"Matrix L2C1:",L2C1);
        RealMatrix L1C2 = MatrixUtils.createRealMatrix(new double[][]{{1, 2}});
        MatricesOperationsReview.this.displayRealMatrix("lets build a matrix 1 x 2", 
                "RealMatrix L1C2 = MatrixUtils.createRealMatrix(new double[][]{{1, 2}})", "Matrix L1C2:",L1C2);
        RealMatrix L2C2 = MatrixUtils.createRealMatrix(new double[][]{{1,2},{3,4}});
        MatricesOperationsReview.this.displayRealMatrix("lets build a matrix 2 x 2", 
                "RealMatrix L2C2 = MatrixUtils.createRealMatrix(new double[][]{{1,2},{3,4}})", "Matrix L1C1:",L2C2);
        RealMatrix L2C4 = MatrixUtils.createRealMatrix(new double[][]{{1,2,3,4},{5,6,7,8}});
        MatricesOperationsReview.this.displayRealMatrix("lets build a matrix 2 x 4", 
                "RealMatrix L2C4 = MatrixUtils.createRealMatrix(new double[][]{{1,2,3,4},{5,6,7,8}})", 
                "Matrix L2C4:",L2C4);
        RealMatrix L3C4 = attachOnesRow(L2C4);
        MatricesOperationsReview.this.displayRealMatrix("lets attach an extra row of ones to it", 
                "RealMatrix L3C4 = attachOnesRow(L2C4)", 
                "Matrix L3C4:",L3C4);
        RealMatrix L2C5 = attachOnesColumn(L2C4);
        MatricesOperationsReview.this.displayRealMatrix("lets attach an extra column of ones instead", 
                "RealMatrix L2C5 = attachOnesRow(L2C4)", 
                "Matrix L2C5:",L2C5);
        L3C4 = attachZerosRow(L2C4);
        MatricesOperationsReview.this.displayRealMatrix("lets attach an extra row of zeros instead", 
                "L3C4 = attachZerosRow(L2C4)", 
                "Matrix L3C4:",L3C4);
        L2C5 = attachZerosColumn(L2C4);
        MatricesOperationsReview.this.displayRealMatrix("lets attach an extra column of zeros instead", 
                "L2C5 = attachZerosColumn(L2C4)", 
                "Matrix L2C5:",L2C5);

/*
        // operations
        System.out.println("Some matrices operations.");
        
        RealMatrix A = new Array2DRowRealMatrix(new double[][]{{1,2,3}, {4,5,6}});
        displayRealMatrix("lets build a matrix", "Matrix A:",A);
        try {
            displayRealMatrix("let's multiply A to A", "Matrix A * A:", A.multiply(A));
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
        displayRealMatrix("lets trasnpose it", "Matrix A':",A.transpose());
        displayRealMatrix("Now we can multiply both","Matrix A * A':",A.multiply(A.transpose()));
        
        RealMatrix W = new Array2DRowRealMatrix(new double[][]{{0.2, -0.5, 0.1, 2.0},
            {1.5,1.3,2.1,0}, {0,0.25,0.2,-0.3}});
        System.out.println("Display W:");
        displayRealMatrix(W);
        RealVector x = new ArrayRealVector(new double[]{56, 231, 24, 2});
        System.out.println("Display x:");
        displayRealVector(x);
        System.out.println("Display b:");
        RealVector b = new ArrayRealVector(new double[]{1.1, 3.2, -1.2});
        displayRealVector(b);
        RealMatrix R = W.multiply(new Array2DRowRealMatrix(x.toArray()));
        System.out.println("Display W * x");
        displayRealMatrix(R);
        System.out.println("Display W * x + b");
        displayRealMatrix(R.add(new Array2DRowRealMatrix(b.toArray())));
        */
}

    void displayRealMatrix(String title, String javaCode, String metadata, RealMatrix M) {
        System.out.println("task: " + title);
        System.out.println("Code: " + javaCode);
        MatricesOperationsReview.this.displayRealMatrix(metadata, M);
    }

    void displayRealMatrix(String title, String metadata, RealMatrix M) {
        System.out.println(title);
        MatricesOperationsReview.this.displayRealMatrix(metadata, M);
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

    void displayRealVector(RealVector v) {
        for(int l=0; l< v.getDimension(); l++) {
            System.out.println(String.format(" %8.2f", v.getEntry(l)));
        }
        System.out.println("\n");
    }

    RealMatrix attachZerosRow(RealMatrix M) {
        return attachValueRow(M, 0D);
    }

    RealMatrix attachOnesRow(RealMatrix M) {
        return attachValueRow(M, 1D);
    }

    RealMatrix attachValueRow(RealMatrix M, double value) {
        int rows = M.getRowDimension();
        int columns = M.getColumnDimension();
        double[][] data = M.getData();
        double[][] newData = new double[rows+1][columns];
        for(int r=0; r<rows; r++) {
            System.arraycopy(data[r], 0, newData[r], 0, columns);
        }
        for(int c = 0; c<columns; c++) {
            newData[rows][c] = value;
        }
        return MatrixUtils.createRealMatrix(newData);
    } 

    RealMatrix attachValueColumn(RealMatrix M, double value) {
        return attachValueRow(M.transpose(), value).transpose();
    }

    RealMatrix attachZerosColumn(RealMatrix M) {
        return attachValueColumn(M, 0D);
    }

    RealMatrix attachOnesColumn(RealMatrix M) {
        return attachValueColumn(M, 1D);
    }

    public static void main(String [] args) {
        MatricesOperationsReview matricesOperationsReview = new MatricesOperationsReview();
    }
}
