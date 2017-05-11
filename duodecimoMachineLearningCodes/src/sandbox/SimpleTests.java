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

import static java.lang.Double.max;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import util.matrix.DuodecimoMatrixUtils;
import util.matrix.DuodecimoVectorUtils;

/**
 *
 * @author duo
 */
public class SimpleTests {

    public SimpleTests() {
        execute();
    }

    final void execute() {
        RealVector realVector = new ArrayRealVector(new double[]{-11.0d, 2.0d, 3.0d, 4.0d});
        System.out.println(DuodecimoVectorUtils.showRealVector("Create a vector:", realVector));
        realVector.mapToSelf(new Maximum());
        System.out.println(DuodecimoVectorUtils.showRealVector("Set a maximum:", realVector));
        double loss = realVector.dotProduct(realVector.map(new Ones()));
        System.out.println("got a loss of: " + loss);
        System.out.println(DuodecimoVectorUtils.showRealVector("from dot to:", 
                realVector.map(new Ones())));
        RealVector realVector2 = new ArrayRealVector(new double[]{-11.0d, 2.0d, 3.0d, 4.0d});
        System.out.println(DuodecimoVectorUtils.showRealVector("Create a vector:", realVector2));
        realVector2.mapToSelf(new test());
        System.out.println(DuodecimoVectorUtils.showRealVector("after test:", realVector2));
        
        RealMatrix M1 = 
        MatrixUtils.createRealMatrix(new double[][] { {1, 2, 3}, {4, 5, 6} });

        RealMatrix M2 = 
        MatrixUtils.createRealMatrix(new double[][] { {1, 2, 3, 4}, 
                {5, 6, 7, 8}, {9, 10, 11, 12 } });
        
        String result = DuodecimoMatrixUtils.showRealMatrix("M1: ", M1);
        result = result.concat(DuodecimoMatrixUtils.showRealMatrix("M2: ", M2));
        result = result.concat(DuodecimoMatrixUtils.showRealMatrix("M1 X M2: ", M1.multiply(M2)));
        System.out.println(result);
    }

    private static class Maximum implements UnivariateFunction {
        @Override
        public double value(double x) {
            return max(0.0d, x);
        }
    }

    private static class Ones implements UnivariateFunction {
        @Override
        public double value(double x) {
            return 1.0d;
        }
    }

    private static class test implements UnivariateFunction {
        int y = 12;
        @Override
        public double value(double x) {
            System.out.println("At index:" + x);
            y++;
            return y;
        }
    }

    public static void main(String[] args) {
        SimpleTests simpleTests = new SimpleTests();
    }
}
