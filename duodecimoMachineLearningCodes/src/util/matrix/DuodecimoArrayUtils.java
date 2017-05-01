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

/**
 *
 * @author duo
 */
public class DuodecimoArrayUtils {

    double[] maximum(double[] v1, double[]v2) {
        double[] v3 = new double[v1.length];
        for(int i=0; i<v1.length; i++) {
            v3[i] = v1[i]>v2[i] ? v1[i] : v2[i];
        }
        return v3;
    }

    double[] maximumOverZero(double[] v1) {
        double[] v3 = new double[v1.length];
        for(int i=0; i<v1.length; i++) {
            v3[i] = v1[i]>0.0d ? v1[i] : 0.0d;
        }
        return v3;
    }

}
