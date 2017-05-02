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
package util.matrix;

import java.util.Arrays;
import java.util.logging.Logger;
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

    private static final Logger LOGGER = Logger.getGlobal();

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

    public static String showRealMatrix(RealMatrix M) {
        String output;
        output = "\n";
        output = output.concat(showRealMatrix(M, -1, -1));
        return (output.concat("\n"));
    }

    public static String showRealMatrix(String header, RealMatrix M) {
        String output;
        output = "\n";
        output = output.concat(showRealMatrix(header, M, -1, -1));
        return (output.concat("\n"));
    }

    public static String showRealMatrix(String header, RealMatrix M, int maxRows, int maxCols) {
        String output;
        output = "\n";
        output = output.concat(header);
        output = output.concat(showRealMatrix(M, maxRows, maxCols));
        return (output.concat("\n"));
    }

    public static String showRealMatrix(RealMatrix M, int maxRows, int maxCols) {
        String output;
        output = "\n".concat("(" + M.getRowDimension() + ", " + 
                M.getColumnDimension() + ")").concat("\n");
        if(maxRows == -1) {
            // show all rows
            maxRows = M.getRowDimension();
        }
        // show first batch
        for (int l = 0; l < (maxRows < M.getRowDimension() ?  maxRows/2 : maxRows); l++) {
            output = output.concat(showRealMatrixLine(M.getRowVector(l), maxCols));
        }
        // if not all, reticenses
        if(maxRows < M.getRowDimension()) {
            output = output.concat("     .\n     .\n     .");
        }
        // if not all, show second batch
        if(maxRows < M.getRowDimension()) {
            maxRows -= maxRows / 2;
            for (int l = M.getRowDimension() - maxRows; l < M.getRowDimension(); l++) {
                output = output.concat(showRealMatrixLine(M.getRowVector(l), maxCols));
            }
        }
        // end show
        return(output.concat("\n"));
    }
    
    public static String showRealMatrixLine(RealVector realVector, int maxCols) {
        String output;
        output = "\n";
        if(maxCols == -1) {
            // show all rows
            maxCols = realVector.getDimension();
        }
        // show first batch
        for (int c = 0; c < (maxCols < realVector. getDimension() ? maxCols/2 : maxCols); c++) {
            output = output.concat(String.format(" %8.4f", realVector.getEntry(c)));
        }
        // if not all, reticenses
        if(maxCols < realVector. getDimension()) {
            output = output.concat(" ... ");
        }
        // if not all, show second batch
        if(maxCols < realVector. getDimension()) {
            maxCols -= maxCols/2;
            for (int c = realVector.getDimension() - maxCols; c < realVector.getDimension(); c++) {
                output = output.concat(String.format(" %8.4f", realVector.getEntry(c)));
            }
        }
        // end line
        return(output.concat("\n"));
    }
}
