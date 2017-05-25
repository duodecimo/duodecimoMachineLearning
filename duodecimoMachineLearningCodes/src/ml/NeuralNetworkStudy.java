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

import java.io.IOException;
import java.util.stream.DoubleStream;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.random.JDKRandomGenerator;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import util.matrix.DuodecimoMatrixUtils;

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
        double[] r = new double[pointsPerClass]; // radius
        double interval = (1.0d - 0.0d) / (double)(pointsPerClass - 1);
        r[0] = 0d;
        for (int l = 1; l < pointsPerClass; l++) {
            r[l] = r[l - 1] + interval;
        }
        System.out.println("R: (interval = " + interval + ")");
        for (int l = 0; l < pointsPerClass; l++) {
            System.out.print(r[l]);
            if(l>0 & l%8==0) System.out.println("");
            else System.out.print(", ");
        }
        System.out.println("");
        
        for (int j = 0; j < numberOfClasses; j++) {
            double[] t = new double[pointsPerClass]; // theta
            interval = (((j + 1) * 4) - (j * 4)) / (pointsPerClass - 1);
            t[0] = j * 4;
            for (int l = 1; l < pointsPerClass; l++) {
                t[l] = t[l - 1] + interval;
            }
            DoubleStream doubleStream = new JDKRandomGenerator((int) System.currentTimeMillis()).
                    doubles(pointsPerClass, -1.0d, 1.0d);
            double[] doubles = doubleStream.toArray();
            for (int l = 0; l < pointsPerClass; l++) {
                t[l] += (doubles[l]/1.5d);
            }

            System.out.println("doubles " + j + ":");
            for (int l = 0; l < pointsPerClass; l++) {
                System.out.print(doubles[l]);
                if (l>0 & l % 8 == 0) {
                    System.out.println("");
                } else {
                    System.out.print(", ");
                }
            }
            System.out.println("");

            System.out.println("t" + j + ":");
            for (int l = 0; l < pointsPerClass; l++) {
                System.out.print(t[l]);
                if (l>0 & l % 8 == 0) {
                    System.out.println("");
                } else {
                    System.out.print(", ");
                }
            }
            System.out.println("");


            for (int ix = pointsPerClass * j, k=0; ix < pointsPerClass * (j + 1); ix++, k++) {
                X.setEntry(ix, 0, r[k]*Math.sin(t[k]));
                X.setEntry(ix, 1, r[k]*Math.cos(t[k]));
                Y.setEntry(ix, j);
            }
        }
        System.out.println(DuodecimoMatrixUtils.showRealMatrix("X:", X, 20, X.getColumnDimension()));
        showChart();
    }

    private XYDataset createJFreeChartDataset(RealMatrix X) {
        XYSeriesCollection result = new XYSeriesCollection();
        XYSeries series = new XYSeries("Cat A");
        for (int i = 0; i < 100; i++) {
            series.add(X.getEntry(i, 0), X.getEntry(i, 1));
        }
        result.addSeries(series);
        series = new XYSeries("Cat B");
        for (int i = 100; i < 200; i++) {
            series.add(X.getEntry(i, 0), X.getEntry(i, 1));
        }
        result.addSeries(series);
        series = new XYSeries("Cat C");
        for (int i = 200; i < 300; i++) {
            series.add(X.getEntry(i, 0), X.getEntry(i, 1));
        }
        result.addSeries(series);
        return result;
    }

    private void showChart() {
        JFreeChart chart = ChartFactory.createScatterPlot(
            "Scatter Plot", // chart title
            "X", // x axis label
            "Y", // y axis label
            createJFreeChartDataset(X), // data
            PlotOrientation.VERTICAL,
            //PlotOrientation.HORIZONTAL,
            true, // include legend
            true, // tooltips
            false // urls
            );
        XYPlot plot = (XYPlot) chart.getPlot();
        ValueAxis yAxis = plot.getRangeAxis();
        yAxis.setRange(-1.0d, 1.0d);
        ValueAxis xAxis = plot.getDomainAxis();
        xAxis.setRange(-1.0d, 1.0d);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setMaximumDrawHeight(400);
        chartPanel.setMinimumDrawHeight(10);
        chartPanel.setMaximumDrawWidth(600);
        chartPanel.setMinimumDrawWidth(10);
        JFrame frame = new JFrame("Neural Network Study");
        frame.getContentPane().add(chartPanel);
        frame.pack();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    /**
     *
     * @param args command line parameters if needed.
     * @throws IOException if received from class,
     */
    public static void main(String[] args) throws IOException {
        NeuralNetworkStudy neuralNetworkStudy = new NeuralNetworkStudy();
    }
}
