/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.matrix;

import java.util.Arrays;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

/**
 * Duodecimo Matrix Utils
 * 
 * Usefull methods
 * 
 * As for now it seems there is no conveninece methods for, i.e. growing matrices 
 * dimensions in commons-math3-3.6.1 library from Apache Foundation, and I need 
 * some, I decided to write them.
 * 
 * Portuguese Version
 * 
 * Métodos Úteis
 * 
 * Como até o momento parece não haver métodos de conveniência para, por exemplo,
 * aumentar as dimensões de matrizes na biblioteca commons-math3-3.6.1 da 
 * Apache Foundation, e eu preciso de alguns, decidí escrevê-los.
 * 
 * @author duo
 */
public class DuodecimoMatrixUtils {

    /**
     * Takes an original matrix as input and returns a new matrix with all data 
     * from the original in the same relative positions to the original matrix 
     * ones plus a new row attatched to it's end, zero filled.
     * 
     * @param M a matrix of type org.apache.commons.math3.linear.RealMatrix
     * @return a new matrix object of type org.apache.commons.math3.linear.RealMatrix
     * 
     * Portuguese version
     * 
     * Recebe uma matriz original como entrada e retorna uma nova matriz com todos
     * os dados da matriz original nas mesmas posições relativas da matriz original
     * mais uma nova linha adicionada no final com todos os elementos zerados.
     * 
     * param M uma matriz do tipo org.apache.commons.math3.linear.RealMatrix
     * return um novo objeto matriz do tipo org.apache.commons.math3.linear.RealMatrix
     */
    public static RealMatrix attachZerosRow(RealMatrix M) {
        return attachValueRow(M, 0D);
    }

    /**
     * Takes an original matrix as input and returns a new matrix with all data 
     * from the original in the same relative positions to the original matrix 
     * ones plus a new row attatched to it's end, one filled.
     * 
     * @param M a matrix of type org.apache.commons.math3.linear.RealMatrix
     * @return a new matrix object of type org.apache.commons.math3.linear.RealMatrix
     * 
     * Portuguese version
     * 
     * Recebe uma matriz original como entrada e retorna uma nova matriz com todos
     * os dados da matriz original nas mesmas posições relativas da matriz original
     * mais uma nova linha adicionada no final com todos os elementos iguais a um.
     * 
     * param M uma matriz do tipo org.apache.commons.math3.linear.RealMatrix
     * return um novo objeto matriz do tipo org.apache.commons.math3.linear.RealMatrix
     */
    public static RealMatrix attachOnesRow(RealMatrix M) {
        return attachValueRow(M, 1D);
    }

    /**
     * Takes an original matrix as input and returns a new matrix with all data 
     * from the original in the same relative positions to the original matrix 
     * ones plus a new row attatched to it's end, value filled.
     * 
     * @param M a matrix of type org.apache.commons.math3.linear.RealMatrix
     * @param value double value to fill the new row.
     * @return a new matrix object of type org.apache.commons.math3.linear.RealMatrix
     * 
     * Portuguese version
     * 
     * Recebe uma matriz original como entrada e retorna uma nova matriz com todos
     * os dados da matriz original nas mesmas posições relativas da matriz original
     * mais uma nova linha adicionada no final com todos os elementos iguas a value.
     * 
     * param M uma matriz do tipo org.apache.commons.math3.linear.RealMatrix
     * param value double value to fill the new row.
     * return um novo objeto matriz do tipo org.apache.commons.math3.linear.RealMatrix
     */
    public static RealMatrix attachValueRow(RealMatrix M, double value) {
        int rows = M.getRowDimension();
        int columns = M.getColumnDimension();
        double[][] data = M.getData();
        double[][] newData = new double[rows+1][columns];
        for(int r=0; r<rows; r++) {
            System.arraycopy(data[r], 0, newData[r], 0, columns);
        }
        Arrays.fill(newData[rows], value);
        return MatrixUtils.createRealMatrix(newData);
    } 

    /**
     * Takes an original matrix as input and returns a new matrix with all data 
     * from the original in the same relative positions to the original matrix 
     * ones plus a new column attatched to it's end, value filled.
     * 
     * @param M a matrix of type org.apache.commons.math3.linear.RealMatrix
     * @param value double value to fill the new column.
     * @return a new matrix object of type org.apache.commons.math3.linear.RealMatrix
     * 
     * Portuguese version
     * 
     * Recebe uma matriz original como entrada e retorna uma nova matriz com todos
     * os dados da matriz original nas mesmas posições relativas da matriz original
     * mais uma nova coluna adicionada no final com todos os elementos iguas a value.
     * 
     * param M uma matriz do tipo org.apache.commons.math3.linear.RealMatrix
     * param value double value to fill the new row.
     * return um novo objeto matriz do tipo org.apache.commons.math3.linear.RealMatrix
     */
    public static RealMatrix attachValueColumn(RealMatrix M, double value) {
        return attachValueRow(M.transpose(), value).transpose();
    }

    /**
     * Takes an original matrix as input and returns a new matrix with all data 
     * from the original in the same relative positions to the original matrix 
     * ones plus a new column attatched to it's end, zeros filled.
     * 
     * @param M a matrix of type org.apache.commons.math3.linear.RealMatrix
     * @return a new matrix object of type org.apache.commons.math3.linear.RealMatrix
     * 
     * Portuguese version
     * 
     * Recebe uma matriz original como entrada e retorna uma nova matriz com todos
     * os dados da matriz original nas mesmas posições relativas da matriz original
     * mais uma nova coluna adicionada no final com todos os elementos iguas a zero.
     * 
     * param M uma matriz do tipo org.apache.commons.math3.linear.RealMatrix
     * return um novo objeto matriz do tipo org.apache.commons.math3.linear.RealMatrix
     */
    public static RealMatrix attachZerosColumn(RealMatrix M) {
        return attachValueColumn(M, 0D);
    }

    /**
     * Takes an original matrix as input and returns a new matrix with all data 
     * from the original in the same relative positions to the original matrix 
     * ones plus a new column attatched to it's end, ones filled.
     * 
     * @param M a matrix of type org.apache.commons.math3.linear.RealMatrix
     * @return a new matrix object of type org.apache.commons.math3.linear.RealMatrix
     * 
     * Portuguese version
     * 
     * Recebe uma matriz original como entrada e retorna uma nova matriz com todos
     * os dados da matriz original nas mesmas posições relativas da matriz original
     * mais uma nova coluna adicionada no final com todos os elementos iguas a um.
     * 
     * param M uma matriz do tipo org.apache.commons.math3.linear.RealMatrix
     * return um novo objeto matriz do tipo org.apache.commons.math3.linear.RealMatrix
     */
    public static RealMatrix attachOnesColumn(RealMatrix M) {
        return attachValueColumn(M, 1D);
    }

    public static void showRealMatrix(RealMatrix M, int maxRows, int maxCols) {
        if(maxRows == -1) {
            // show all rows
            maxRows = M.getRowDimension();
        }
        // show first batch
        for (int l = 0; l < (maxRows < M.getRowDimension() ?  maxRows/2 : maxRows); l++) {
            showRealMatrixLine(M.getRowVector(l), maxCols);
        }
        // if not all, reticenses
        if(maxRows < M.getRowDimension()) {
            System.out.println("     .\n     .\n     .");
        }
        // if not all, show second batch
        if(maxRows < M.getRowDimension()) {
            maxRows -= maxRows / 2;
            for (int l = M.getRowDimension() - maxRows; l < M.getRowDimension(); l++) {
                showRealMatrixLine(M.getRowVector(l), maxCols);
            }
        }
        // end show
        System.out.println("");
    }
    
    private static void showRealMatrixLine(RealVector realVector, int maxCols) {
        if(maxCols == -1) {
            // show all rows
            maxCols = realVector.getDimension();
        }
        // show first batch
        for (int c = 0; c < (maxCols < realVector. getDimension() ? maxCols/2 : maxCols); c++) {
            System.out.print(String.format(" %8.4f", realVector.getEntry(c)));
        }
        // if not all, reticenses
        if(maxCols < realVector. getDimension()) {
            System.out.print(" ... ");
        }
        // if not all, show second batch
        if(maxCols < realVector. getDimension()) {
            maxCols -= maxCols/2;
            for (int c = realVector.getDimension() - maxCols; c < realVector.getDimension(); c++) {
                System.out.print(String.format(" %8.4f", realVector.getEntry(c)));
            }
        }
        // end line
        System.out.println("");
    }
}
