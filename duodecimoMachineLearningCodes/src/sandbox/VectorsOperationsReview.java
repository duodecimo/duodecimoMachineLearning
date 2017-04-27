/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sandbox;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import util.matrix.Display;
import util.matrix.DuodecimoVectorUtils;

/**
 *
 * @author duo
 */
public class VectorsOperationsReview {

    public VectorsOperationsReview() throws Exception {
        // vector normalization
        RealVector realVector = new ArrayRealVector(new double[]{128, 44, 199, 88, 255, 99, 31, 0, 127, 75, 164});
        Display.displayRealVector("Antes de normalizar:", realVector);
        realVector = DuodecimoVectorUtils.normalizeAndScaleToRange(realVector, new double[]{-1,1});
        Display.displayRealVector("Após normalizar:", realVector);
    }

    public static void main(String[] args) throws Exception {
        VectorsOperationsReview vectorsOperationsReview = new VectorsOperationsReview();
    }
}
