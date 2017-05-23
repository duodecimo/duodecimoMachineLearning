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
package ml;

import java.util.List;
import java.util.stream.DoubleStream;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.random.JDKRandomGenerator;

/**
 *
 * @author duo
 */
public class NeuralNetworkStudy {

/*
    Lets generate a classification dataset that is not easily linearly separable. Our favorite example is the spiral dataset, which can be generated as follows:
    N = 100 # number of points per class
    D = 2 # dimensionality
    K = 3 # number of classes
    X = np.zeros((N*K,D)) # data matrix (each row = single example)
    y = np.zeros(N*K, dtype='uint8') # class labels
    for j in xrange(K):
    ix = range(N*j,N*(j+1))
    r = np.linspace(0.0,1,N) # radius
    t = np.linspace(j*4,(j+1)*4,N) + np.random.randn(N)*0.2 # theta
    X[ix] = np.c_[r*np.sin(t), r*np.cos(t)]
    y[ix] = j
    # lets visualize the data:
    plt.scatter(X[:, 0], X[:, 1], c=y, s=40, cmap=plt.cm.Spectral)
     */
    int pointsPerClass = 100;
    int dimensionality = 2;
    int numberOfClasses = 3;
    // matrix with all entries = 0.0d, data matrix (each row = single example)
    RealMatrix X = MatrixUtils.createRealMatrix(pointsPerClass* numberOfClasses , dimensionality);
    // vector with all entries = 0.0d, class labels
    RealVector Y = new ArrayRealVector(pointsPerClass * numberOfClasses);
    public NeuralNetworkStudy() {
        for(int j=0; j < numberOfClasses; j++) {
            int k = pointsPerClass*(j+1) - pointsPerClass*j;
            int[] ix = new int[k];
            for(int l = pointsPerClass*j, m=0; l<pointsPerClass*(j+1); l++ ) {
                ix[m++] = l;
            }
            double[] r = new double[pointsPerClass]; // radius
            double interval = (1-0)/(pointsPerClass-1);
            r[0] = 0d;
            for(int l = 1; l < pointsPerClass; l++) {
                r[l] = r[l-1]+interval;
            }
            double[] t = new double[pointsPerClass]; // theta
            interval = (((j+1)*4) - (j*4))/(pointsPerClass-1);
            t[0] = j*4;
            for(int l = 1; l < pointsPerClass; l++) {
                t[l] = t[l-1] * interval;
            }
            DoubleStream doubleStream = new JDKRandomGenerator((int) System.currentTimeMillis()).
                    doubles(pointsPerClass);
            double[] doubles = doubleStream.toArray();
            for(int l = 0; l < pointsPerClass; l++) {
                t[l]+= doubles[l]*0.2d;
            }
            for(int i=0; i<pointsPerClass; i++) {
                X.setEntry(i, 0, Math.sin(t[i]));
                X.setEntry(i, 1, Math.cos(t[i]));
                Y.setEntry(i, j);
            }
        }
    }
}
