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
import org.apache.commons.math3.linear.RealVector;
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
        System.out.println(DuodecimoVectorUtils.showRealVector("from dot to:", realVector.map(new Ones())));
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


    public static void main(String[] args) {
        SimpleTests simpleTests = new SimpleTests();
    }
}
