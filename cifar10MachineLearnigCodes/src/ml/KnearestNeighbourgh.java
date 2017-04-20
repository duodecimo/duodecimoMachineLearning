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
import java.util.ArrayList;
import java.util.List;

/**
 * The class KnearestNeighbourgh uses the nearest neighbourgh algorithm to classify
 * images from CIFAR-10 dataset.
 * 
 * The k-nearest neighbourgh (knn) algorithm will check a testing image against a set of
 * trainning images, to find out witch of the k trainning images, k representing 
 * a integer value, (i.e k ={1, 2, 3, ...}), are the closest to the testing, and then
 * classify the testing with the same class of the trainning image that appeared
 * most among this k closests.
 * 
 * For example, suppose k = 5. After calculating the distances between a trainning
 * image Te[i], against all Tr[m] images, you get this 5 closest distances:
 * 
 * closestDistances[0] = 121 (comparing to image Tr[323], labeled label[3])
 * closestDistances[1] = 247 (comparing to image Tr[479], labeled label[7])
 * closestDistances[2] = 349 (comparing to image Tr[632], labeled label[2])
 * closestDistances[3] = 451 (comparing to image Tr[789], labeled label[7])
 * closestDistances[4] = 658 (comparing to image Tr[931], labeled label[4])
 * 
 * If it was the nn , that takes the closest distance, the test image 
 * TE[i] would be classified as from the class label[3]. In fact, if we make k = 1,
 * we must have the same result for the knn as the nn algorithm.
 * 
 * But, as in our example we are considering k = 5, even if the closest distance
 * found was 121, the label that most appeared among the five closests is label[7],
 * that appeared in distance[1] and distance[3]. In this example it appeared twice, 
 * while the other labels appeared once each. In case there is a draw, we then pick
 * the closest label among the drawing ones.
 * 
 * Each Cifar-10 images has 32x32x3 bytes, = 3072 bytes.
 * If we want to calculate distances betwwen a testing image and a trainnig image,
 * the same as we did in the nn.
 * That will be the sum, for i going from 0 to 3071 of mod(Te[i] - Tr[i]). 
 * The result will be a numeric value.
 * 
 * @see NearestNeighbourgh()
 * 
 * @author duo
 */
public class KnearestNeighbourgh {
        RealMatrix Xtr, Ytr, Xte, Yte;
        Cifar10Utils cifar10Utils;

    public KnearestNeighbourgh(int k) throws IOException {
        if(k>2) System.out.println("Running " + k + "-nearestNeighbourgh");
        else System.out.println("Running nearestNeighbourgh");
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
        kNearestNeighbourEvaluation(k);
        System.exit(0);
    }

    final void kNearestNeighbourEvaluation(int k) throws IOException {
        double distance, distanceSwap;
        double[] closestDistances;
        double[] closestTrains;
        double[] closestLabels;
        int[] closestLabelsTimes;
        double accuracy = 0D;
        double[] sumTr;
        double[] sumTe;
        double[] util;
        boolean hit = false;
        int maxDisplays = 90;
        // for each image in tests
        for(int test = 0; test < Cifar10Utils.TOT_TESTS; test++) {
            sumTe = Xte.getRow(test);
            closestDistances= new double[k];
            closestTrains= new double[k];
            closestLabels = new double[k];
            int mostOcurredLabelIndex;
            closestLabelsTimes = new int[10];
            // initialization
            for(int i=0; i<k; i++) {
                closestDistances[i] = Double.MAX_VALUE;
                closestTrains[i] = Double.MAX_VALUE;
                closestLabels[i] = Double.MAX_VALUE;
            }
            // calculate distance from each training
            for(int train=0; train < Cifar10Utils.TOT_TRAINNINGS; train++) {
                sumTr = Xtr.getRow(train);
                distance = 0D;
                for(int i=0; i<Cifar10Utils.TOT_BYTES; i++) {
                    distance+=((double) abs(sumTe[i]-sumTr[i]));
                }
                // select the k closests
                for (int i = 0; i < k; i++) {
                    if (distance < closestDistances[i]) {
                        // swap
                        distanceSwap = distance;
                        distance = closestDistances[i];
                        closestDistances[i] = distanceSwap;
                        util = Ytr.getRow(train);
                        closestLabels[i] = util[0];
                        closestTrains[i] = train;
                    }
                }
            }
            util = Yte.getRow(test);
            // clean closestLabelsTimes
            for(int l=0; l<10; l++) {
                closestLabelsTimes[l] = 0;
            }
            // count how many times labels appeared in k closestDistances
            for(int l=0; l<k; l++) {
                closestLabelsTimes[(int) closestLabels[l]] ++;
            }
            // pick among the k closest distances the label that ocurred most times
            mostOcurredLabelIndex = 0;
            for(int l=0; l<9; l++) {
                if(closestLabelsTimes[l+1] > closestLabelsTimes[l]) {
                    mostOcurredLabelIndex = l+1;
                }
            }
            System.out.println("most occurences: " + mostOcurredLabelIndex + " with " + closestLabelsTimes[mostOcurredLabelIndex]);
            //System.exit(0);
            // the test was classified with label pickedK!
            if(util[0] == mostOcurredLabelIndex) {
                accuracy++;
            }
            /*
            // lets display some hits and fails alternating them:
            // shows a pair of fails alternanting with a pair of hittings
            if(util[0] == labelAppearances[(int) pickedK]) {
                if (!hit && maxDisplays-->0) {
                    cifar10Utils.displayImage(Xtr.getRow((int) closestTrains[(int) pickedK]), closestLabels[(int) pickedK]);
                    cifar10Utils.displayImage(Xte.getRow(test), util[0]);
                    hit = !hit; // toggle
                }
            } else {
                if (hit && maxDisplays-->0) {
                    cifar10Utils.displayImage(Xtr.getRow((int) closestTrains[(int) pickedK]), closestLabels[(int) pickedK]);
                    cifar10Utils.displayImage(Xte.getRow(test), util[0]);
                    hit = !hit; // toggle
                }
            }
            */
            //System.out.println("test: " +test +
            //        " distancia minima: " + closestDistances +
            //        " label teste: " + util[0] +
            //        " label minimo: " + closestLabels +
            //        "  hittings up to now: " + accuracy);
        }
        System.out.println("Accuracy = " + (accuracy * 100 /(Cifar10Utils.TOT_TESTS)) + "%");
    }

    public static void main(String[] args) throws IOException {
        KnearestNeighbourgh nn = new KnearestNeighbourgh(5);
    }
}