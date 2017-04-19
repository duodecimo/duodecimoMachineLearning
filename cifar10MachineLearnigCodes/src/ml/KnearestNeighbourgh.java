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
import static java.lang.Math.abs;

/**
 * The class KnearestNeighbourgh uses the nearest neighbourgh algorithm to classify
 * images from CIFAR-10 dataset.
 * 
 * The k-nearest neighbourgh (knn) algorithm will check a testing image against a set of
 * trainning images, to find out witch of the k trainning images, k representing 
 * a integer value, (i.e k ={1, 2, 3, ...}, are the closest to the testing, and then
 * classify the testing with the same class of the trainning image that appeared
 * amost among this k closests.
 * 
 * For example, suppose k = 5. After calculating the distances between a trainning
 * image Te[i], against all Tr[m] images, you get as the 5 least distances:
 * distance[0] = 121 (comparing to image Tr[323], labeled label[3]
 * distance[1] = 247 (comparing to image Tr[479], labeled label[7]
 * distance[2] = 121 (comparing to image Tr[632], labeled label[2]
 * distance[3] = 121 (comparing to image Tr[789], labeled label[7]
 * distance[4] = 121 (comparing to image Tr[931], labeled label[4]
 * 
 * If it was the nn neighbourg, that takes the closest distance, the test image 
 * TE[i] would be classified as from the class label[3]. In fact, if we make k = 1,
 * we must have the same result for the knn as the nn algorithm.
 * 
 * But, as in our example we are consideing k = 5, even if the closest distance found
 * was 121, the label that most appeared among the five closests is label[7], that
 * appeared in distance[1] and distance[3]. In this example it appeared twice, while
 * the other labels appeared once each.
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
        //  Xtr (of size 50,000 x 32 x 32 x 3) holds all the images in the training set,
        // and a corresponding 1-dimensional array Ytr (of length 50,000) holds the training labels
        // (from 0 to 9)
        // Xtr = TOT_TRAINNINGS x TOT_PIXELS
        // Xte and Yte do the same for the testing set.
        RealMatrix Xtr, Ytr, Xte, Yte;
        Cifar10Utils cifar10Utils;

    public KnearestNeighbourgh(int k) throws IOException {
        cifar10Utils = new Cifar10Utils();
        Xtr = cifar10Utils.getXtr();
        Xte = cifar10Utils.getXte();
        Ytr = cifar10Utils.getYtr();
        Yte = cifar10Utils.getYte();
        kNearestNeighbourEvaluation(k);
        System.exit(0);
    }

    final void kNearestNeighbourEvaluation(int k) throws IOException {
        // perform k-nearest neighbour
        // if k=1, it is equivalent to nearest neighbourgh:
        // The algorithm chooses the label with the least distance between each
        // test and the train images set.
        // when k>2, it checks the k least distances between each
        // test and the train images set, and then chooses the label
        // with greater number of selections.
        // When draws happens there must be a tie-braker criteria.
        // In this case, the natural choice is to pick the minimal
        // distance between the tie labels. This is why if k = 2, it will
        // perform the same way as k=1.
        double distance;
        double distanceSwap;
        double[] minDistance;
        double[] minTrain;
        double choosenTrain;
        double[] minLabel;
        double choosenLabel;
        double accuracy = 0D;
        double[] sumTr;
        double[] sumTe;
        double[] util;
        double choosenK;
        int countingK;
        int anteriorCountingK;
        boolean hit = false;
        int maxDisplays = 90;
        // for each image in tests
        if(k>2) System.out.println("Running " + k + "-nearestNeighbourgh");
        else System.out.println("Running nearestNeighbourgh");
        for(int test = 0; test < Cifar10Utils.TOT_TESTS; test++) {
            sumTe = Xte.getRow(test);
            minDistance= new double[k]; //Double.MAX_VALUE;
            minTrain= new double[k]; //Double.MAX_VALUE;
            minLabel = new double[k];
            // initialization
            for(int i=0; i<k; i++) {
                minDistance[i] = Double.MAX_VALUE;
                minTrain[i] = Double.MAX_VALUE;
                minLabel[i] = Double.MAX_VALUE;
            }
            // calculate distance from each training
            for(int train=0; train < Cifar10Utils.TOT_TRAINNINGS; train++) {
                sumTr = Xtr.getRow(train);
                distance = 0D;
                for(int i=0; i<Cifar10Utils.TOT_BYTES; i++) {
                    distance+=((double) abs(sumTe[i]-sumTr[i]));
                }
                for (int i = 0; i < k; i++) {
                    if (distance < minDistance[i]) {
                        // swap
                        distanceSwap = distance;
                        distance = minDistance[i];
                        minDistance[i] = distanceSwap;
                        util = Ytr.getRow(train);
                        minLabel[i] = util[0];
                        minTrain[i] = train;
                    }
                }
            }
            util = Yte.getRow(test);
            // pick the label that occured most
            choosenK = 0;
            countingK = 0;
            anteriorCountingK = 0;
            for(int i = 0; i<k-1; i++) {
                if(minDistance[i+1] == minDistance[i]) {
                    countingK ++;
                    if(countingK > anteriorCountingK) {
                        choosenK = i+1;
                    }
                } else {
                    anteriorCountingK = countingK;
                    countingK = 1;
                }
            }
            if(minLabel[(int) choosenK] == util[(int) 0]) {
                accuracy++;
                if (hit && maxDisplays-->0) {
                    cifar10Utils.displayImage(Xtr.getRow((int) minTrain[(int) choosenK]), minLabel[(int) choosenK]);
                    cifar10Utils.displayImage(Xte.getRow(test), util[0]);
                    hit = !hit; // toggle
                }
            } else {
                if (!hit && maxDisplays-->0) {
                    cifar10Utils.displayImage(Xtr.getRow((int) minTrain[(int) choosenK]), minLabel[(int) choosenK]);
                    cifar10Utils.displayImage(Xte.getRow(test), util[0]);
                    hit = !hit; // toggle
                }
            }
            // lets display some hits and fails alternating them
            System.out.println("test: " +test +
                    " distancia minima: " + minDistance +
                    " label teste: " + util[0] +
                    " label minimo: " + minLabel +
                    "  hittings up to now: " + accuracy);
        }
        System.out.println("Accuracy = " + (accuracy * 100 /(Cifar10Utils.TOT_TESTS)) + "%");
    }

    public static void main(String[] args) throws IOException {
        KnearestNeighbourgh nn = new KnearestNeighbourgh(5);
    }
}