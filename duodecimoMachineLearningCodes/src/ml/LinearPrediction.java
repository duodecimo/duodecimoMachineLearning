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
import org.apache.commons.math3.random.JDKRandomGenerator;

/**
 *
 * @author duo
 */
public class LinearPrediction {
        private final RealMatrix Xtr, Ytr, Xte, Yte;
        private final Cifar10Utils cifar10Utils;
        private static final Logger LOGGER = Logger.getGlobal();
    public LinearPrediction() throws IOException {
        LOGGER.setLevel(Level.INFO);
        LOGGER.log(Level.INFO, "Running Linear Prediction");
        /**
         * The class cifar10.Cifar10Utils from this package is well documented
         * and it is strongly recomended the reading of its comments explainning
         * how it works, what it does and why before going further.
        */
        cifar10Utils = new Cifar10Utils();
        Xtr = cifar10Utils.getXtr();
        Xte = cifar10Utils.getXte();
        Ytr = cifar10Utils.getYtr();
        Yte = cifar10Utils.getYte();
        linearPredictionWithRandomSearch();
    }

    public void linearPredictionWithRandomSearch() {
        RealMatrix BestW = null; // to hold the best random generated weights
        float bestloss = Float.MAX_VALUE, loss;
        for(int i=0; i<150; i++) { // number of guesses
            DoubleStream doubleStream = new JDKRandomGenerator((int) System.currentTimeMillis()).
                    doubles((cifar10Utils.getTotalOfBytes()+1) * cifar10Utils.getNames().length);
            double[] doubles = doubleStream.toArray();
            // generate random W gaussian
            RealMatrix W = MatrixUtils.createRealMatrix(cifar10Utils.getNames().length,
                    cifar10Utils.getTotalOfBytes()+1);
            int k=0;
            for(int row=0; row<cifar10Utils.getNames().length; row++) {
                for(int col=0; col<cifar10Utils.getTotalOfBytes()+1;col++) {
                    W.setEntry(row, col, doubles[k++]);
                }
            }
            W=W.scalarMultiply(0.0001d);
            loss=0.0f;
            for (int j = 0; j < cifar10Utils.getTotalOfTrainnings(); j++) {
                // loop to visit all trainnings
                // notice below that we need to append a 1 to the end of the image 
                // vector for the bias trick
                int index = (int) Ytr.getEntry(j, 0);
                RealVector x = Xtr.getRowVector(j).append(1.0d);
                loss += lossFunctionUnvectorized(x, (int) index, W);
            }
            if(loss<bestloss) {
                bestloss = loss;
                BestW = W.copy();
            }
            System.out.println(String.format("in attempt %d the loss was %f, best %f %c", i+1, loss, bestloss, '%'));
        }
        // BestW holds the weigths
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
            loss += Double.max(0, scores.getEntry(i) - correctClassScore + delta) * 0.0002d;
        }
        return loss;
    } 

    public static void main(String[] args) throws IOException {
        LinearPrediction linearPrediction = new LinearPrediction();
    }}
