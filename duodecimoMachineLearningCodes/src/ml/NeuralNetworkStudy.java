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
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import util.matrix.DuodecimoVectorUtils;

/**
 *
 * @author duo
 */
public class NeuralNetworkStudy {
    private final boolean DEBUGCOMPAREPYTHON = false;
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

            if (!DEBUGCOMPAREPYTHON) {
                for (int ix = pointsPerClass * j, k = 0; ix < pointsPerClass * (j + 1); ix++, k++) {
                    X.setEntry(ix, 0, r[k] * Math.sin(t[k]));
                    X.setEntry(ix, 1, r[k] * Math.cos(t[k]));
                    Y.setEntry(ix, j);
                }
            }
        }
        if (DEBUGCOMPAREPYTHON) {
            readMatrixFromTxt(X, Y);
        }
        System.out.println(DuodecimoMatrixUtils.showRealMatrix("X:", X, 20, X.getColumnDimension()));
        System.out.println(DuodecimoVectorUtils.showRealVector("Y:", Y, 20));
        /* now we can plot the dataset in order to see taht it is not liearly separable.
        */
        showChart();
        //System.exit(0);
        trainingSoftmaxLinearClassifier();
    }

    private void trainingSoftmaxLinearClassifier() {
        
        // lets crete a weight matrix W and a bias vector b
        RealMatrix W = MatrixUtils.createRealMatrix(dimensionality, numberOfClasses); // 2X3
        RealVector b = new ArrayRealVector(numberOfClasses); // 1X3 zeroes vector
        if (DEBUGCOMPAREPYTHON) {
            // create W with fixed values
            W = MatrixUtils.createRealMatrix(new double[][]{{0.01d, 0.0d, -0.01d},{0.0d, -0.01d, 0.01d}});
        }
        else {
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
        }
        System.out.println(DuodecimoMatrixUtils.showRealMatrix("W:", W));
        RealMatrix Scores;
        Scores = X.multiply(W);
        System.out.println(DuodecimoMatrixUtils.showRealMatrix("Scores:", Scores, 10, -1));
        for(int i=0; i<Scores.getRowDimension(); i++) {
            for(int j=0; j< Scores.getColumnDimension(); j++) {
                Scores.setEntry(i, j, (Scores.getEntry(i, j)+b.getEntry(j)));
            }
        }
        System.out.println(DuodecimoMatrixUtils.showRealMatrix("Scores:", Scores, 10, -1));
        // scores debug: using read python data from file, it is equivalent!

        // some hyperparameters
        double stepSize = 1e-0;
        double regularization = 1e-3; // regularization strength
        /*
            int pointsPerClass = 100;
            int dimensionality = 2;
            int numberOfClasses = 3;
        */
        // gradient descent loop
        RealVector divisor;
        RealMatrix ExpScores;
        double rowSum;
        RealMatrix Probabilities;
        RealVector logProbs;
        double sumLogProbs;
        double dataLoss;
        double regularizedLoss;
        double wSum;
        double loss;
        RealMatrix DW = null;
        RealVector db;
        RealMatrix DScores;
        for(int i=0; i<200; i++) {
            // evaluate class scores, [pointsPerClass x numberOfClasses]
            Scores = X.multiply(W);
            for (int row = 0; row < Scores.getRowDimension(); row++) {
                for (int col = 0; col < Scores.getColumnDimension(); col++) {
                    Scores.setEntry(row, col, (Scores.getEntry(row, col) + b.getEntry(col)));
                }
            }
            if(i<1) {
                System.out.println(DuodecimoMatrixUtils.showRealMatrix("Scores:", Scores, 10, -1));
                // scores debug: using read python data from file, it is equivalent! (05/31/2017)
            }
            // compute the class probabilities
            divisor = new ArrayRealVector(Scores.getRowDimension()); // zeros vector of size pointsPerClass
            ExpScores = Scores.copy(); // [pointsPerClass x numberOfClasses]
            for(int row = 0; row < ExpScores.getRowDimension(); row++) {
                rowSum = 0;
                for(int col = 0; col < ExpScores.getColumnDimension(); col++) {
                    ExpScores.setEntry(row, col, Math.exp(ExpScores.getEntry(row, col)));
                    rowSum+=ExpScores.getEntry(row, col);
                }
                divisor.setEntry(row, rowSum);
            }
            if(i<1) {
                System.out.println(DuodecimoMatrixUtils.showRealMatrix("ExpScores:", ExpScores, 10, -1));
                // exp_scores debug: using read python data from file, it is equivalent! (06/01/2017)
            }
            Probabilities = ExpScores.copy();
            for(int row=0; row<Probabilities.getRowDimension(); row++) {
                Probabilities.setRowVector(row, Probabilities.getRowVector(row).
                        mapDivide(divisor.getEntry(row)));
            }
            if(i<1) {
                System.out.println(DuodecimoMatrixUtils.showRealMatrix("Probabilities:", Probabilities, 10, -1));
                // probs debug: using read python data from file, it is equivalent! (06/01/2017)
            }
            /*
                    att: probs[range(num_examples),y]
                         will select from each probs row (range(num_examples))
                         the value of the column of the correct class (y)
                         returning a vector of num_examples elements
            */
            logProbs = new ArrayRealVector(X.getRowDimension()); // zeros vector
            sumLogProbs=0;
            for(int row=0; row<Probabilities.getRowDimension(); row++) {
                logProbs.setEntry(row, -Math.log(Probabilities.getEntry(row, (int) Y.getEntry(row))));
                sumLogProbs+=logProbs.getEntry(row);
            }
            dataLoss = sumLogProbs/X.getRowDimension();
            if(i<1) {
                System.out.println(String.format("\nData Loss = %f\n\n", dataLoss));
                // data_loss debug: using read python data from file, it is equivalent! (06/01/2017)
            }
            wSum = 0.0d;
            for(int row=0; row<W.getRowDimension(); row++) {
                for(int col=0; col<W.getColumnDimension(); col++) {
                    wSum+=(W.getEntry(row, col)*W.getEntry(row, col));
                }
            }
            regularizedLoss = 0.5d * regularization * wSum;
            if(i<1) {
                System.out.println(String.format("\nRegularized loss = %f\n\n", regularizedLoss));
                // check! (python 2e-07)
            }
            loss = dataLoss + regularizedLoss;
            if(i<1) {
                System.out.println(String.format("\nLoss = %f\n\n", loss));
                // loss debug: using read python data from file, it is equivalent! (06/01/2017)
            }
            if(i%10==0) {
                System.out.println(String.format("iteration %d: loss %f", i, loss));
                // loss debug: using read python data from file,
                // all iterations are equivalent! (06/01/2017)
            }
            // compute the gradient on scores
            DScores = Probabilities.copy();
            for(int row=0; row<DScores.getRowDimension(); row++) {
                DScores.setEntry(row, (int) Y.getEntry(row), 
                        DScores.getEntry(row, (int) Y.getEntry(row))-1.0d);
                for(int col=0; col<DScores.getColumnDimension(); col++) {
                    DScores.setEntry(row, col,
                            DScores.getEntry(row, col) / X.getRowDimension());
                }
            }
            if(i<1) {
                System.out.println(DuodecimoMatrixUtils.showRealMatrix("DScores:", DScores, 10, -1));
                // dscores debug: using read python data from file, it is equivalent! (06/01/2017)
            }
            // backpropate the gradient to the parameters (W,b)
            DW = X.transpose().multiply(DScores);
            db = new ArrayRealVector(b.getDimension()); // zeros vector
            for(int row=0; row<DScores.getRowDimension(); row++) {
                for(int col=0; col<DScores.getColumnDimension(); col++) {
                    db.setEntry(col, (db.getEntry(col)+DScores.getEntry(row, col)));
                }
            }
            DW = DW.add(W.scalarMultiply(regularization));
            // perform a parameter update
            W = W.add(DW.scalarMultiply(-stepSize));
            b = b.add(db.mapMultiply(-stepSize));
        }
        /*
            # evaluate training set accuracy
            scores = np.dot(X, W) + b
            predicted_class = np.argmax(scores, axis=1)
            print 'training accuracy: %.2f%%' % (np.mean(predicted_class == y)*100)
        */
        // evaluate training set accuracy
        Scores = X.multiply(W);
        for (int row = 0; row < Scores.getRowDimension(); row++) {
            for (int col = 0; col < Scores.getColumnDimension(); col++) {
                Scores.setEntry(row, col, (Scores.getEntry(row, col) + b.getEntry(col)));
            }
        }
        double prediction;
        double predictionColumn;
        double accuracy = 0.0d;
        for (int row = 0; row < Scores.getRowDimension(); row++) {
            prediction = Double.MIN_VALUE;
            predictionColumn = -1;
            for(int col=0; col<Scores.getColumnDimension(); col++) {
                if(Scores.getEntry(row, col)>prediction) {
                    prediction = Scores.getEntry(row, col);
                    predictionColumn = col;
                }
            }
            if(predictionColumn == Y.getEntry(row)) {
                accuracy++;
            }
        }
        System.out.println(String.format("\n\nAccuracy: %6.2f%%\n", (accuracy*100/X.getRowDimension())));
    }

    private void trainingNeuralNetwork() {
        // size of the hidden layer
        int hiddenLayerSize = 100;
        // lets crete a weight matrix W and a bias vector b
        RealMatrix W = MatrixUtils.createRealMatrix(dimensionality, numberOfClasses); // 2X3
        RealVector b = new ArrayRealVector(hiddenLayerSize); // 1X100
        // lets crete a weight matrix W2 and a bias vector b2 for hidden layer
        RealMatrix W2 = MatrixUtils.createRealMatrix(hiddenLayerSize, numberOfClasses); // 100X3
        RealVector b2 = new ArrayRealVector(numberOfClasses); // 1X3
        if (DEBUGCOMPAREPYTHON) {
            // create W with fixed values
            W = MatrixUtils.createRealMatrix(new double[][]{{0.01d, 0.0d, -0.01d},{0.0d, -0.01d, 0.01d}});
        }
        else {
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
        }
        System.out.println(DuodecimoMatrixUtils.showRealMatrix("W:", W));
        RealMatrix Scores;
        Scores = X.multiply(W);
        System.out.println(DuodecimoMatrixUtils.showRealMatrix("Scores:", Scores, 10, -1));
        for(int i=0; i<Scores.getRowDimension(); i++) {
            for(int j=0; j< Scores.getColumnDimension(); j++) {
                Scores.setEntry(i, j, (Scores.getEntry(i, j)+b.getEntry(j)));
            }
        }
        System.out.println(DuodecimoMatrixUtils.showRealMatrix("Scores:", Scores, 10, -1));
        // scores debug: using read python data from file, it is equivalent!

        // some hyperparameters
        double stepSize = 1e-0;
        double regularization = 1e-3; // regularization strength
        /*
            int pointsPerClass = 100;
            int dimensionality = 2;
            int numberOfClasses = 3;
        */
        // gradient descent loop
        RealVector divisor;
        RealMatrix ExpScores;
        double rowSum;
        RealMatrix Probabilities;
        RealVector logProbs;
        double sumLogProbs;
        double dataLoss;
        double regularizedLoss;
        double wSum;
        double loss;
        RealMatrix DW = null;
        RealVector db;
        RealMatrix DScores;
        for(int i=0; i<200; i++) {
            // evaluate class scores, [pointsPerClass x numberOfClasses]
            Scores = X.multiply(W);
            for (int row = 0; row < Scores.getRowDimension(); row++) {
                for (int col = 0; col < Scores.getColumnDimension(); col++) {
                    Scores.setEntry(row, col, (Scores.getEntry(row, col) + b.getEntry(col)));
                }
            }
            if(i<1) {
                System.out.println(DuodecimoMatrixUtils.showRealMatrix("Scores:", Scores, 10, -1));
                // scores debug: using read python data from file, it is equivalent! (05/31/2017)
            }
            // compute the class probabilities
            divisor = new ArrayRealVector(Scores.getRowDimension()); // zeros vector of size pointsPerClass
            ExpScores = Scores.copy(); // [pointsPerClass x numberOfClasses]
            for(int row = 0; row < ExpScores.getRowDimension(); row++) {
                rowSum = 0;
                for(int col = 0; col < ExpScores.getColumnDimension(); col++) {
                    ExpScores.setEntry(row, col, Math.exp(ExpScores.getEntry(row, col)));
                    rowSum+=ExpScores.getEntry(row, col);
                }
                divisor.setEntry(row, rowSum);
            }
            if(i<1) {
                System.out.println(DuodecimoMatrixUtils.showRealMatrix("ExpScores:", ExpScores, 10, -1));
                // exp_scores debug: using read python data from file, it is equivalent! (06/01/2017)
            }
            Probabilities = ExpScores.copy();
            for(int row=0; row<Probabilities.getRowDimension(); row++) {
                Probabilities.setRowVector(row, Probabilities.getRowVector(row).
                        mapDivide(divisor.getEntry(row)));
            }
            if(i<1) {
                System.out.println(DuodecimoMatrixUtils.showRealMatrix("Probabilities:", Probabilities, 10, -1));
                // probs debug: using read python data from file, it is equivalent! (06/01/2017)
            }
            /*
                    att: probs[range(num_examples),y]
                         will select from each probs row (range(num_examples))
                         the value of the column of the correct class (y)
                         returning a vector of num_examples elements
            */
            logProbs = new ArrayRealVector(X.getRowDimension()); // zeros vector
            sumLogProbs=0;
            for(int row=0; row<Probabilities.getRowDimension(); row++) {
                logProbs.setEntry(row, -Math.log(Probabilities.getEntry(row, (int) Y.getEntry(row))));
                sumLogProbs+=logProbs.getEntry(row);
            }
            dataLoss = sumLogProbs/X.getRowDimension();
            if(i<1) {
                System.out.println(String.format("\nData Loss = %f\n\n", dataLoss));
                // data_loss debug: using read python data from file, it is equivalent! (06/01/2017)
            }
            wSum = 0.0d;
            for(int row=0; row<W.getRowDimension(); row++) {
                for(int col=0; col<W.getColumnDimension(); col++) {
                    wSum+=(W.getEntry(row, col)*W.getEntry(row, col));
                }
            }
            regularizedLoss = 0.5d * regularization * wSum;
            if(i<1) {
                System.out.println(String.format("\nRegularized loss = %f\n\n", regularizedLoss));
                // check! (python 2e-07)
            }
            loss = dataLoss + regularizedLoss;
            if(i<1) {
                System.out.println(String.format("\nLoss = %f\n\n", loss));
                // loss debug: using read python data from file, it is equivalent! (06/01/2017)
            }
            if(i%10==0) {
                System.out.println(String.format("iteration %d: loss %f", i, loss));
                // loss debug: using read python data from file,
                // all iterations are equivalent! (06/01/2017)
            }
            // compute the gradient on scores
            DScores = Probabilities.copy();
            for(int row=0; row<DScores.getRowDimension(); row++) {
                DScores.setEntry(row, (int) Y.getEntry(row), 
                        DScores.getEntry(row, (int) Y.getEntry(row))-1.0d);
                for(int col=0; col<DScores.getColumnDimension(); col++) {
                    DScores.setEntry(row, col,
                            DScores.getEntry(row, col) / X.getRowDimension());
                }
            }
            if(i<1) {
                System.out.println(DuodecimoMatrixUtils.showRealMatrix("DScores:", DScores, 10, -1));
                // dscores debug: using read python data from file, it is equivalent! (06/01/2017)
            }
            // backpropate the gradient to the parameters (W,b)
            DW = X.transpose().multiply(DScores);
            db = new ArrayRealVector(b.getDimension()); // zeros vector
            for(int row=0; row<DScores.getRowDimension(); row++) {
                for(int col=0; col<DScores.getColumnDimension(); col++) {
                    db.setEntry(col, (db.getEntry(col)+DScores.getEntry(row, col)));
                }
            }
            DW = DW.add(W.scalarMultiply(regularization));
            // perform a parameter update
            W = W.add(DW.scalarMultiply(-stepSize));
            b = b.add(db.mapMultiply(-stepSize));
        }
        /*
            # evaluate training set accuracy
            scores = np.dot(X, W) + b
            predicted_class = np.argmax(scores, axis=1)
            print 'training accuracy: %.2f%%' % (np.mean(predicted_class == y)*100)
        */
        // evaluate training set accuracy
        Scores = X.multiply(W);
        for (int row = 0; row < Scores.getRowDimension(); row++) {
            for (int col = 0; col < Scores.getColumnDimension(); col++) {
                Scores.setEntry(row, col, (Scores.getEntry(row, col) + b.getEntry(col)));
            }
        }
        double prediction;
        double predictionColumn;
        double accuracy = 0.0d;
        for (int row = 0; row < Scores.getRowDimension(); row++) {
            prediction = Double.MIN_VALUE;
            predictionColumn = -1;
            for(int col=0; col<Scores.getColumnDimension(); col++) {
                if(Scores.getEntry(row, col)>prediction) {
                    prediction = Scores.getEntry(row, col);
                    predictionColumn = col;
                }
            }
            if(predictionColumn == Y.getEntry(row)) {
                accuracy++;
            }
        }
        System.out.println(String.format("\n\nAccuracy: %6.2f%%\n", (accuracy*100/X.getRowDimension())));
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

    private void readMatrixFromTxt(RealMatrix X, RealVector Y) {
        try {
            List<String> lines = Files.readAllLines(
                    Paths.get("/home/duo/NetBeansProjects/gitHub/"
                            + "duodecimoMachineLearning/duodecimoMachineLearningCodes/"
                            + "python/Xdata.txt"), Charset.forName("UTF-8"));
            double[][] values = new double[X.getRowDimension()][X.getColumnDimension()];
            String[] v;
            int row=0;
            for(String line : lines) {
                v = line.split(", ");
                X.setEntry(row, 0, new Double(v[0]));
                X.setEntry(row, 1, new Double(v[1]));
                row++;
            }
            lines = Files.readAllLines(
                    Paths.get("/home/duo/NetBeansProjects/gitHub/"
                            + "duodecimoMachineLearning/duodecimoMachineLearningCodes/"
                            + "python/Ydata.txt"), Charset.forName("UTF-8"));
            row=0;
            for(String line : lines) {
                Y.setEntry(row, new Double(line));
                row++;
            }
        } catch (IOException ex) {
            Logger.getLogger(NeuralNetworkStudy.class.getName()).log(Level.SEVERE, null, ex);
        }
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
