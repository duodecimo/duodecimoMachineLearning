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

import org.apache.commons.math3.linear.RealVector;

/**
 * Duodecimo Vector Utils
 * 
 * Usefull methods
 * 
 * As for now it seems there is no conveninece methods for, i.e. normalize and
 * scale vectors in commons-math3-3.6.1 library from Apache Foundation, and I need 
 * some, I decided to write them.
 * 
 * Portuguese Version
 * 
 * Métodos Úteis
 * 
 * Como até o momento parece não haver métodos de conveniência para, por exemplo,
 * normalizar e escalar vetores na biblioteca commons-math3-3.6.1 da 
 * Apache Foundation, e eu preciso de alguns, decidí escrevê-los.
 * 
 * @author duo
 */
public class DuodecimoVectorUtils {
    /**
     * Given a vector x and a range a to b,
     * uses the formula "xi = (b-a) * (xi - xmin) / (xmax - xmin) + a" to
     * replace all vectors entries and returns the normalized and scaled vector.
     * 
     * Portuguese Version
     * 
     * Dado um vetor x e um intevalo a para b,
     * usa a fórmula "xi = (b-a) * (xi - xmin) / (xmax - xmin) + a" para
     * atualizar todas as entradas do vetor e retorna o vetor normalizado e escalado.
     * 
     * @param realVector a vector (um vetor)
     * @param range a range, in the form new double[]{double, double}, i.e.,
     * new double(){-1.0, 1.0}. (um intervalo, na forma
     * new double[]{double, double}, por exemplo,
     * new double(){-1.0, 1.0}.)
     * @return a scaled and normalized RealVector
     * @throws Exception if it receives a invalid range.
     */
    public static final RealVector normalizeAndScaleToRange(RealVector realVector, double[] range) 
            throws Exception {
        if(range.length != 2) {
            throw new Exception(
                    "Invalid range exception: use form new double[]{double, double}, "
                            + "i.e. new double[]{-1.0, 1.0}");
        }
        double xMax = realVector.getMaxValue();
        double xMin = realVector.getMinValue();
        for(int i = 0; i < realVector.getDimension(); i++) {
            realVector.setEntry(i, doubleNormalizeAndScaleToRange(realVector.getEntry(i), range, xMax, xMin));
        }
        return realVector;
    }

    public static final double doubleNormalizeAndScaleToRange(double d, double[] range, double xMax, double xMin) {
        d = ((range[1] - range[0]) * (d - xMin) / (xMax - xMin)) + range[0];
        return d;
    }

    public static void showRealVector(String header, RealVector realVector) {
        System.out.println(header);
        showRealVector(realVector, -1);
    }

    public static void showRealVector(String header, RealVector realVector, int maxElements) {
        System.out.println(header);
        showRealVector(realVector, maxElements);
    }

    public static void showRealVector(RealVector realVector) {
        showRealVector(realVector, -1);
    }

    public static void showRealVector(RealVector realVector, int maxElements) {
        if(maxElements == -1) {
            // show all elements
            maxElements = realVector.getDimension();
        }
        // show first batch
        for (int c = 0; c < (maxElements < realVector. getDimension() ? maxElements/2 : maxElements); c++) {
            System.out.println(String.format(" %8.4f", realVector.getEntry(c)));
        }
        // if not all, reticenses
        if(maxElements < realVector. getDimension()) {
            System.out.println("     .\n     .\n     .");
        }
        // if not all, show second batch
        if(maxElements < realVector. getDimension()) {
            maxElements -= maxElements/2;
            for (int c = realVector.getDimension() - maxElements; c < realVector.getDimension(); c++) {
                System.out.println(String.format(" %8.4f", realVector.getEntry(c)));
            }
        }
        // end line
        System.out.println("");
    }
}
