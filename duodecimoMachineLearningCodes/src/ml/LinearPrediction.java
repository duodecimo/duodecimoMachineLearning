/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml;

import cifar10.Cifar10Utils;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.DoubleStream;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.random.CorrelatedRandomVectorGenerator;
import org.apache.commons.math3.random.GaussianRandomGenerator;
import org.apache.commons.math3.random.JDKRandomGenerator;
import org.apache.commons.math3.random.NormalizedRandomGenerator;
import org.apache.commons.math3.random.RandomAdaptor;
import util.matrix.Display;

/**
 *
 * @author duo
 */
public class LinearPrediction {
//        private final RealMatrix Xtr, Ytr, Xte, Yte;
//        private final Cifar10Utils cifar10Utils;
        private static final Logger LOGGER = Logger.getGlobal();
    public LinearPrediction() throws IOException {
        LOGGER.setLevel(Level.INFO);
        LOGGER.log(Level.INFO, "Running Linear Prediction");
        linearPredictionWithRandomSearch();
        /**
         * The class cifar10.Cifar10Utils from this package is well documented
         * and it is strongly recomended the reading of its comments explainning
         * how it works, what it does and why before going further.
        */
//        cifar10Utils = new Cifar10Utils();
//        Xtr = cifar10Utils.getXtr();
//        Xte = cifar10Utils.getXte();
//        Ytr = cifar10Utils.getYtr();
 //       Yte = cifar10Utils.getYte();
    }

    public void linearPredictionWithRandomSearch() {
        float bestloss = Float.MAX_VALUE;
        //for(int i=0; i<1000; i++) {
            DoubleStream doubleStream = new JDKRandomGenerator().doubles(2073*10);
            double[] doubles = doubleStream.toArray();
            // generate random W gaussian
            RealMatrix W = MatrixUtils.createRealMatrix(2073, 10);
            int k=0;
            for(int row=0; row<2073; row++) {
                for(int col=0; col<10;col++) {
                    W.setEntry(row, col, doubles[k++]);
                }
            }
            W=W.scalarMultiply(0.0001d);
            Display.displayRealMatrix("Show W", W);
        //}
    }

    public float lossFunctionUnvectorized(RealVector x, int y, RealMatrix W) {
        float delta = 1.0f;
        RealVector scores = W.operate(x);
        double correctClassScore = scores.getEntry(y);
        int d = W.getRowDimension();
        float loss = 0.0f;
        for(int i=0; i<d; i++) {
            if(i == correctClassScore) {
                continue;
            }
            loss += Double.max(0, scores.getEntry(i) - correctClassScore + delta);
        }
        return loss;
    } 

    public static void main(String[] args) throws IOException {
        LinearPrediction linearPrediction = new LinearPrediction();
    }}
