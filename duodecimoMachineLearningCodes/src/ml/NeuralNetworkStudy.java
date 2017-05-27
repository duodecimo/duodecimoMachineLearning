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

import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
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
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
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
    int pointsPerClass = 100;
    int dimensionality = 2;
    int numberOfClasses = 3;
    // matrix with all entries = 0.0d, data matrix (each row = single example)
    RealMatrix X = MatrixUtils.createRealMatrix(pointsPerClass* numberOfClasses , dimensionality);
    // vector with all entries = 0.0d, class labels
    RealVector Y = new ArrayRealVector(pointsPerClass * numberOfClasses);
    public NeuralNetworkStudy() throws IOException {
        /*
        Lets generate a classification dataset that is not easily linearly separable.
        A dataset with 3 classes where each one makes a spiral shape.
        */
        double[] r = new double[pointsPerClass]; // radius
        double interval = (1.0d - 0.0d) / (double)(pointsPerClass - 1);
        r[0] = 0d;
        for (int l = 1; l < pointsPerClass; l++) {
            r[l] = r[l - 1] + interval;
        }
        /* debug
        System.out.println("R: (interval = " + interval + ")");
        for (int l = 0; l < pointsPerClass; l++) {
            System.out.print(r[l]);
            if(l>0 & l%8==0) System.out.println("");
            else System.out.print(", ");
        }
        System.out.println("");
        */
        for (int j = 0; j < numberOfClasses; j++) {
            double[] t = new double[pointsPerClass]; // theta
            interval = (double) (((j + 1) * 4) - (j * 4)) / (double) (pointsPerClass - 1);
            t[0] = j * 4;
            for (int l = 1; l < pointsPerClass; l++) {
                t[l] = t[l - 1] + interval;
            }
            /* debug
            System.out.println("t pure " + j + "(interval = " + interval +"(:");
            for (int l = 0; l < pointsPerClass; l++) {
                System.out.print(t[l]);
                if (l>0 & l % 8 == 0) {
                    System.out.println("");
                } else {
                    System.out.print(", ");
                }
            }
            System.out.println("");
            */
            DoubleStream doubleStream = new JDKRandomGenerator((int) System.currentTimeMillis()).
                    doubles(pointsPerClass, -1.0d, 1.0d);
            double[] doubles = doubleStream.toArray();
            for (int l = 0; l < pointsPerClass; l++) {
                t[l] += (doubles[l]*0.2d);
            }

            /* debug
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
            */

            for (int ix = pointsPerClass * j, k=0; ix < pointsPerClass * (j + 1); ix++, k++) {
                X.setEntry(ix, 0, r[k]*Math.sin(t[k]));
                X.setEntry(ix, 1, r[k]*Math.cos(t[k]));
                Y.setEntry(ix, j);
            }
        }
        /* debug
        System.out.println(DuodecimoMatrixUtils.showRealMatrix("X:", X, 20, X.getColumnDimension()));
        */
        /* now we can plot the dataset in order to see taht it is not liearly separable.
        */
        showChart();
        
        // lets crete a weight matrix W and a bias vector b
        RealMatrix W = MatrixUtils.createRealMatrix(dimensionality, numberOfClasses);
        RealVector b = new ArrayRealVector(numberOfClasses);
        // lets generate random values to initialize W
        JDKRandomGenerator generator = new JDKRandomGenerator((int) System.currentTimeMillis());
        DoubleStream doubleStream;
        double[] doubles;
        for(int i=0; i<W.getRowDimension(); i++) {
            doubleStream = generator.doubles(numberOfClasses, -1.0d, 1.0d);
            doubles = doubleStream.toArray();
            for(int j=0; j< numberOfClasses; j++) {
                W.setEntry(i, j, doubles[j]*0.01d);
            }
        }
        System.out.println(DuodecimoMatrixUtils.showRealMatrix("W:", W));
        RealMatrix Scores = X.multiply(W);
        System.out.println(DuodecimoMatrixUtils.showRealMatrix("Scores:", Scores, 10, -1));
        for(int i=0; i<Scores.getRowDimension(); i++) {
            for(int j=0; j< Scores.getColumnDimension(); j++) {
                Scores.setEntry(i, j, (Scores.getEntry(i, j)+b.getEntry(j)));
            }
        }
        System.out.println(DuodecimoMatrixUtils.showRealMatrix("Scores:", Scores, 10, -1));

        // some hyperparameters
        double stepSize = 1e-0;
        double reg = 1e-3; // regularization strength
        // gradient descent loop
        int numExamples = X.getRowDimension();
        for(int i=0; i<200; i++) {
            // evaluate class scores, [N x K]
            Scores = X.multiply(W);
            for (int l = 0; l < Scores.getRowDimension(); l++) {
                for (int c = 0; c < Scores.getColumnDimension(); c++) {
                    Scores.setEntry(l, c, (Scores.getEntry(l, c) + b.getEntry(c)));
                }
            }
            /*
            # compute the class probabilities
            exp_scores = np.exp(scores)
            probs = exp_scores / np.sum(exp_scores, axis=1, keepdims=True) # [N x K]
            */
            // compute the class probabilities
            //RealMatrix ExpScores = Scores;
            //double probs = exp_scores / np.sum(exp_scores, axis=1, keepdims=True) # [N x K];
        }
    }

    private XYDataset createJFreeChartDataset(RealMatrix X) {
        // in order to use JFreeChart to scatter plot the data,
        // we can store our data in a XYSeriesCollection object
        // the we add to it K XYSeries objects, each one
        // holding a class
        XYSeriesCollection result = new XYSeriesCollection();
        XYSeries series = new XYSeries("Class A");
        for (int i = 0; i < 100; i++) {
            series.add(X.getEntry(i, 0), X.getEntry(i, 1));
        }
        result.addSeries(series);
        series = new XYSeries("Class B");
        for (int i = 100; i < 200; i++) {
            series.add(X.getEntry(i, 0), X.getEntry(i, 1));
        }
        result.addSeries(series);
        series = new XYSeries("Class C");
        for (int i = 200; i < 300; i++) {
            series.add(X.getEntry(i, 0), X.getEntry(i, 1));
        }
        result.addSeries(series);
        return result;
    }

    private void showChart() throws IOException {
        JFreeChart chart = ChartFactory.createScatterPlot(
            "Spiral Dataset Plot", // chart title
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
        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.BLACK);
        plot.setDomainGridlinePaint(Color.BLACK);
        //XYItemRenderer renderer = plot.getRenderer();
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(600, 600));
        ChartUtilities.saveChartAsPNG(new File("NeuralNetworkStudy.png"), chart, 
                600, 600);
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
