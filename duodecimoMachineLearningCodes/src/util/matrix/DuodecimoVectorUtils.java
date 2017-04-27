/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
            realVector.setEntry(i,
                    ((range[1] - range[0]) *
                    (realVector.getEntry(i) - xMin) / (xMax - xMin)) + range[0]);
        }
        return realVector;
    }
}
