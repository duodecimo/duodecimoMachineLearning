/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cifar10;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import static java.lang.Math.abs;
import static java.lang.Math.abs;

public class NearestNeighbourgh {
        //  Xtr (of size 50,000 x 32 x 32 x 3) holds all the images in the training set,
        // and a corresponding 1-dimensional array Ytr (of length 50,000) holds the training labels
        // (from 0 to 9)
        // Xtr = TOT_EXAMPLES x TOT_PIXELS
        // Xte and Yte do the same for the testing set.
        RealMatrix Xtr, Ytr, Xte, Yte;
        FileInputStream inputStream;
        private final int TOT_EXAMPLES = 50000;
        //private final int TOT_EXAMPLES = 50000/100;
        private final int TOT_TESTS = 10000;
        //private final int TOT_TESTS = 10000/10;
        private final int TOT_PIXELS = 1024;
        private final int TOT_BYTES = 3072;
        private final int CIFAR_LINE = (1 + 3072);
        private JFrame jFrame;

    public NearestNeighbourgh() throws IOException {
        Xtr = new Array2DRowRealMatrix(TOT_EXAMPLES, TOT_BYTES);
        Ytr = new Array2DRowRealMatrix(TOT_EXAMPLES, 1);
        Xte = new Array2DRowRealMatrix(TOT_TESTS, TOT_BYTES);
        Yte = new Array2DRowRealMatrix(TOT_TESTS, 1);
        int rowOffset = 0;
        readData("./data/data_batch_1.bin", Xtr, Ytr, TOT_EXAMPLES/5, rowOffset);
        rowOffset += TOT_EXAMPLES/5;
        readData("./data/data_batch_2.bin", Xtr, Ytr, TOT_EXAMPLES/5, rowOffset);
        rowOffset += TOT_EXAMPLES/5;
        readData("./data/data_batch_3.bin", Xtr, Ytr, TOT_EXAMPLES/5, rowOffset);
        rowOffset += TOT_EXAMPLES/5;
        readData("./data/data_batch_4.bin", Xtr, Ytr, TOT_EXAMPLES/5, rowOffset);
        rowOffset += TOT_EXAMPLES/5;
        readData("./data/data_batch_5.bin", Xtr, Ytr, TOT_EXAMPLES/5, rowOffset);
        // load test
        rowOffset = 0;
        readData("./data/test_batch.bin", Xte, Yte, TOT_TESTS, rowOffset);
        shuffleMatrix();
        nearestNeighbourEvaluation();
        System.exit(0);
    }

    private void shuffleMatrix() throws IOException {
        Random random = new Random();
        int index1, index2;
        double[] tmpX, tmpY;
        // shuffle examples
        for(int i = 0; i < TOT_EXAMPLES/50; i++) {
            // select 2 random rows
            index1= random.nextInt(TOT_EXAMPLES);
            index2= random.nextInt(TOT_EXAMPLES);
            // swap rows
            tmpX = Xtr.getRow(index1);
            tmpY = Ytr.getRow(index1);
            Xtr.setRow(index1, Xtr.getRow(index2));
            Ytr.setRow(index1, Ytr.getRow(index2));
            Xtr.setRow(index2, tmpX);
            Ytr.setRow(index2, tmpY);
        }
        // show some trains lines
        for(int k = 0; k<10*18; k++) {
            displayImage(Xtr.getRow(k));
        }
        
        // shuffle tests
        for(int i = 0; i < TOT_TESTS/10; i++) {
            // select 2 random rows
            index1= random.nextInt(TOT_TESTS);
            index2= random.nextInt(TOT_TESTS);
            // swap rows
            tmpX = Xte.getRow(index1);
            tmpY = Yte.getRow(index1);
            Xte.setRow(index1, Xte.getRow(index2));
            Yte.setRow(index1, Yte.getRow(index2));
            Xte.setRow(index2, tmpX);
            Yte.setRow(index2, tmpY);
        }
        // show some tests lines
        for(int k = 0; k<5*18; k++) {
            displayImage(Xte.getRow(k));
        }
    }
    final void readLabels() throws FileNotFoundException, IOException {
        System.out.println("Lendo 100 * " + CIFAR_LINE + " para obter labels");
        byte[] b = new byte[100 * CIFAR_LINE];
        double[] label = new double[1];
        inputStream = new FileInputStream("./data/data_batch_1.bin");
        int lidos;
        lidos = inputStream.read(b);
        System.out.println("Lidos: " + lidos);
        for(int row = 0; row < 100; row++) {
            System.out.println("Label (c/c)" + (row+1) + ": " + (b[row*CIFAR_LINE] & 0xFF));
            System.out.println("Label (s/c)" + (row+1) + ": " + (b[row*CIFAR_LINE]));
        }
    }

    final void readData(String fileName, RealMatrix X, RealMatrix Y, 
            int nImages, int initRow) throws FileNotFoundException, IOException {
        byte[] b = new byte[nImages * CIFAR_LINE];
        double[] label = new double[1];
        inputStream = new FileInputStream(fileName);
        int lidos;
        lidos = inputStream.read(b);
        System.out.println("arquivo: "
                + fileName + " lidos: " + lidos
                + " (" + nImages + " * " + (lidos / nImages) + ")");
        System.out.println("deve ler: " + nImages * CIFAR_LINE);
        for(int row = 0; row < nImages; row++) {
            label[0] = b[row*CIFAR_LINE] & 0xFF;
            Y.setRow(row + initRow, label);
            for(int col = 1; col < CIFAR_LINE; col++) {
                X.setEntry(row+initRow, col-1, b[row*CIFAR_LINE + col] & 0xFF);
            }
            System.out.println("row: " + (row + initRow) + " label: " + label[0]);
            /*
            if(row%20==0) {
                displayImage(b);
            }
            */
        }
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
        for(int test = 0; test < TOT_TESTS; test++) {
            sumTe = Xte.getRow(test);
            minDistance=Double.MAX_VALUE;
            // calculate distance from each training
            for(int train=0; train < TOT_EXAMPLES; train++) {
                sumTr = Xtr.getRow(train);
                distance = 0D;
                for(int i=0; i<TOT_BYTES; i++) {
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
        System.out.println("Accuracy = " + (accuracy * 100 /(TOT_TESTS)) + "%");
    }


    public static void main(String[] args) throws IOException {
        NearestNeighbourgh nn = new NearestNeighbourgh();
    }

    public JFrame getjFrame() {
        if (jFrame == null) {
            jFrame = new JFrame("Mostrando imagem");
            jFrame.setLayout(new FlowLayout());
            jFrame.setBounds(100, 100, 680, 630);
        }
       return jFrame;
    }

    /**
     * Displays one image
     * @param b
     * here the values comes from the file, so its size is 3073 bytes
     * note that we should add 1 to create the RGB color (we add 1 when we
     * must discount the label in buffers of length 3073).
     * @throws IOException 
     */
    public void displayImage(byte[] b) throws IOException {
        // just display an image
        System.out.println("Displaying image from b[] lenght: " + b.length);
        BufferedImage image = new BufferedImage(32, 32, BufferedImage.TYPE_INT_RGB);
        for (int row = 0; row < 32; row++) {
            for (int col = 0; col < 32; col++) {
                Color color = new Color(
                        b[1 + 1024 * 0 + row * 32 + col] & 0xFF,
                        b[1 + 1024 * 1 + row * 32 + col] & 0xFF,
                        b[1 + 1024 * 2 + row * 32 + col] & 0xFF);
                image.setRGB(col, row, color.getRGB());
            }
        }
        jFrame = getjFrame();
        jFrame.getContentPane().add(new JLabel(new ImageIcon(image)));
        jFrame.setVisible(true);
        boolean result = ImageIO.write(image, "jpeg", new FileOutputStream("./out.jpg"));
        if (!result) {
            System.err.println("failed");
        }
    }

    /**
     * Displays one image
     * @param b
     * here the values comes from the matrix, so its size is 3072 bytes
     * note that we should not add 1 to create the RGB color (we add 1 when we
     * must discount the label in buffers of length 3073).
     * @throws IOException 
     */
    public void displayImage(double[] b) throws IOException {
        // just display an image
        System.out.println("Displaying image from b[] lenght: " + b.length);
        BufferedImage image = new BufferedImage(32, 32, BufferedImage.TYPE_INT_RGB);
        for (int row = 0; row < 32; row++) {
            for (int col = 0; col < 32; col++) {
                Color color = new Color(
                        ((byte) b[1024 * 0 + row * 32 + col]) & 0xFF,
                        ((byte) b[1024 * 1 + row * 32 + col]) & 0xFF,
                        ((byte) b[1024 * 2 + row * 32 + col]) & 0xFF);
                image.setRGB(col, row, color.getRGB());
            }
        }
        jFrame = getjFrame();
        jFrame.getContentPane().add(new JLabel(new ImageIcon(image)));
        jFrame.setVisible(true);
        boolean result = ImageIO.write(image, "jpeg", new FileOutputStream("./out.jpg"));
        if (!result) {
            System.err.println("failed");
        }
    }
}