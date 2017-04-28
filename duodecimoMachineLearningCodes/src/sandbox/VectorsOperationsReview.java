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
