/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml;

import cifar10.Cifar10Utils;
import java.io.IOException;
import org.apache.commons.math3.linear.RealMatrix;
import static java.lang.Math.abs;

public class NearestNeighbourgh {
        //  Xtr (of size 50,000 x 32 x 32 x 3) holds all the images in the training set,
        // and a corresponding 1-dimensional array Ytr (of length 50,000) holds the training labels
        // (from 0 to 9)
        // Xtr = TOT_EXAMPLES x TOT_PIXELS
        // Xte and Yte do the same for the testing set.
        RealMatrix Xtr, Ytr, Xte, Yte;

    public NearestNeighbourgh() throws IOException {
        Cifar10Utils cifar10Utils = new Cifar10Utils();
        Xtr = cifar10Utils.getXtr();
        Xte = cifar10Utils.getXte();
        nearestNeighbourEvaluation();
        System.exit(0);
    }

    final void nearestNeighbourEvaluation() {
        // perform nearest neighbour
        double distance;
        double minDistance;
        double minLabel=-1D;
        double accuracy = 0D;
        double[] sumTr;
        double[] sumTe;
        double[] util;
        // for each image in tests
        for(int test = 0; test < Cifar10Utils.TOT_TESTS; test++) {
            sumTe = Xte.getRow(test);
            minDistance=Double.MAX_VALUE;
            // calculate distance from each training
            for(int train=0; train < Cifar10Utils.TOT_EXAMPLES; train++) {
                sumTr = Xtr.getRow(train);
                distance = 0D;
                for(int i=0; i<Cifar10Utils.TOT_BYTES; i++) {
                    distance+=((double) abs(sumTe[i]-sumTr[i]));
                }
                if (distance < minDistance) {
                    minDistance = distance;
                    util = Ytr.getRow(train);
                    minLabel = util[0];
                }
            }
            util = Yte.getRow(test);
            if(minLabel == util[0]) accuracy++;
            System.out.println("test: " +test +
                    " distancia minima: " + minDistance +
                    " label teste: " + util[0] +
                    " label minimo: " + minLabel +
                    "  hittings up to now: " + accuracy);
        }
        System.out.println("Accuracy = " + (accuracy * 100 /(Cifar10Utils.TOT_TESTS)) + "%");
    }

    public static void main(String[] args) throws IOException {
        NearestNeighbourgh nn = new NearestNeighbourgh();
    }
}