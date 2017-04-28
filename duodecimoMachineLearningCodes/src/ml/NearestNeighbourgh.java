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

import cifar10.Cifar10Utils;
import java.io.IOException;
import org.apache.commons.math3.linear.RealMatrix;
import java.util.logging.Level;
import java.util.logging.Logger;
import static java.lang.Math.abs;

/**
 * The class NearestNeighbourgh uses the nearest neighbourgh algorithm to classify
 * images from CIFAR-10 dataset.
 * 
 * The nearest neighbourgh (nn) algorithm will check a testing image against a set of
 * trainning images, to find out witch of the trainning images is the closest to
 * the testing, and then classify the testing with the  same class of this closest
 * trainning image.
 * 
 * Each Cifar-10 images has 32x32x3 bytes, = 3072 bytes.
 * If we want to calculate distances betwwen a testing image and a trainnig image.
 * That will be the sum, for i going from 0 to 3071 of mod(Te[i] - Tr[i]). 
 * The result will be a numeric value.
 * The nn algorithm will classify the testing image as belonging to the same class
 * of the closest trainning image.
 * 
 * @author duo
 */
public class NearestNeighbourgh {
        RealMatrix Xtr, Ytr, Xte, Yte;
        Cifar10Utils cifar10Utils;
        private static final Logger LOGGER = Logger.getGlobal();

    public NearestNeighbourgh() throws IOException {
        /**
         * The class cifar10.Cifar10Utils from this package is well documented
         * and it is strongly recomended the reading of its comments explainning
         * how it works, what it does and why before going further.
        */
        LOGGER.log(Level.INFO, java.util.ResourceBundle.getBundle("ml/Bundle").getString("RUNNING NEARESTNEIGHBOURGH"));
        LOGGER.setLevel(Level.INFO);
        cifar10Utils = new Cifar10Utils(false);
        Xtr = cifar10Utils.getXtr();
        Xte = cifar10Utils.getXte();
        Ytr = cifar10Utils.getYtr();
        Yte = cifar10Utils.getYte();
        nearestNeighbourEvaluation();
    }

    final void nearestNeighbourEvaluation() throws IOException {
        // perform nearest neighbour
        double distance;
        double minDistance;
        double minTrain;
        double minLabel=-1D;
        double accuracy = 0D;
        double[] sumTr;
        double[] sumTe;
        double[] util;
        boolean hit = false;
        int maxDisplays = 90;
        // for each image in tests
        for(int test = 0; test < Cifar10Utils.TOT_TESTS; test++) {
            // retrieve a row of test, exactly the 32x32x3 (3072) bytes of a image.
            sumTe = Xte.getRow(test);
            // initialize minDistance with the maximum possible value. A inital low
            // value, that can be lower than all distances, may lead to a wrong result.
            minDistance=Double.MAX_VALUE;
            // to hild the row number where there is the less distance.
            minTrain=Double.MAX_VALUE;
            // calculate distance from each training
            for(int train=0; train < Cifar10Utils.TOT_TRAINNINGS; train++) {
                sumTr = Xtr.getRow(train);
                distance = 0D;
                for(int i=0; i<Cifar10Utils.TOT_BYTES; i++) {
                    distance+=((double) abs(sumTe[i]-sumTr[i]));
                }
                if (distance < minDistance) {
                    minDistance = distance;
                    util = Ytr.getRow(train);
                    minLabel = util[0];
                    minTrain = train;
                }
            }
            util = Yte.getRow(test);
            if(minLabel == util[0]) {
                accuracy++;
                if (hit && maxDisplays-->0) {
                    cifar10Utils.displayImage(Xtr.getRow((int) minTrain), minLabel);
                    cifar10Utils.displayImage(Xte.getRow(test), util[0]);
                    hit = !hit; // toggle
                }
            } else {
                if (!hit && maxDisplays-->0) {
                    cifar10Utils.displayImage(Xtr.getRow((int) minTrain), minLabel);
                    cifar10Utils.displayImage(Xte.getRow(test), util[0]);
                    hit = !hit; // toggle
                }
            }
            // lets display some hits and fails alternating them
            LOGGER.log(Level.FINER, java.util.ResourceBundle.getBundle("ml/Bundle").getString("TEST: {0} CLOSEST DISTANCE: {1} TEST LABEL: {2} ")
                    + java.util.ResourceBundle.getBundle("ml/Bundle").getString("CLOSEST LABEL: {3}  HITTINGS UP TO NOW: {4}"), 
                    new Object[]{test, minDistance, util[0], minLabel, accuracy});
        }
        LOGGER.log(Level.INFO, java.util.ResourceBundle.getBundle("ml/Bundle").getString("ACCURACY = {0}% WITH {1} TESTS AGAINST {2} TRAINNING FOR EXAMPLES."), 
                new Object[]{accuracy * 100 /(Cifar10Utils.TOT_TESTS), Cifar10Utils.TOT_TESTS, 
                    Cifar10Utils.TOT_TRAINNINGS});
        LOGGER.info(java.util.ResourceBundle.getBundle("ml/Bundle").getString("CLOSE THE FRAME TO SHUTDOWN THE APPLICATION."));
    }

    public static void main(String[] args) throws IOException {
        NearestNeighbourgh nn = new NearestNeighbourgh();
    }
}