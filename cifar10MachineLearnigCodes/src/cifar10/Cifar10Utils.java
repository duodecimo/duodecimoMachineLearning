/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cifar10;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

/**
 *
 * @author duo
 * 
 * Cifar10Utils is a class that provides usefull methods to retrieve and process
 * images from the CIFAR_10 images dataset.
 * 
 * What are CIFAR-10 and CIFAR-100?
 * The CIFAR-10 and CIFAR-100 are labeled subsets of the 80 million tiny images 
 * dataset. They were collected by Alex Krizhevsky, Vinod Nair, and Geoffrey Hinton.
 * visit: https://www.cs.toronto.edu/~kriz/cifar.html
 * 
 * The CIFAR-10 dataset consists of 60000 32x32 colour images in 10 classes, with
 * 6000 images per class. There are 50000 training images and 10000 test images.
 * The dataset is divided into five training batches and one test batch, each with 
 * 10000 images. The test batch contains exactly 1000 randomly-selected images from 
 * each class. The training batches contain the remaining images in random order, 
 * but some training batches may contain more images from one class than another. 
 * Between them, the training batches contain exactly 5000 images from each class. 
 * 
 * Cifar10Utils is intended to deal with de binary version of CIFAR-10, downloaded 
 * from https://www.cs.toronto.edu/~kriz/cifar.html.
 * 
 * The binary version contains the files data_batch_1.bin, data_batch_2.bin, ..., 
 * data_batch_5.bin, as well as test_batch.bin. Each of these files is formatted 
 * as follows:
 * <1 x label><3072 x pixel> ...
 * <1 x label><3072 x pixel>
 * In other words, the first byte is the label of the first image, which is a number 
 * in the range 0-9. The next 3072 bytes are the values of the pixels of the image. 
 * The first 1024 bytes are the red channel values, the next 1024 the green, and 
 * the final 1024 the blue. The values are stored in row-major order, so the first 
 * 32 bytes are the red channel values of the first row of the image.
 * 
 * Each file contains 10000 such 3073-byte "rows" of images, although there is nothing 
 * delimiting the rows. Therefore each file should be exactly 30730000 bytes long. 
 * 
 * There is another file, called batches.meta.txt. This is an ASCII file that maps 
 * numeric labels in the range 0-9 to meaningful class names. It is merely a list 
 * of the 10 class names, one per row. The class name on row i corresponds to numeric 
 * label i.
 */
public class Cifar10Utils {

    /**
     * this value will be used as a tax to decide the percentage that will be
     * retrieved from each CIFAR-10 data file.
     * So, a value of 0.1f will retrive only 10% of the images, as a value of 
     * 1.0f will retrieve 100% of the images.
     * To get thrustfull test results comparable to other results made public
     * by people that used CIFAR-10 too, one should use 100% of the data.
     * This variable is here in order to facilitate using less data to check
     * code funcionality, to run in lower memory machines reducing the time of 
     * processing.
     */
    public static final float TAX_OF_IMAGES_FROM_FILES = 0.1f;
    /**
     * @see TAX_OF_IMAGES_FROM_FILES
     * TOT_TRAINNINGS is the total number of trainnings (examples). As for 
     * CIFAR-10 it is 50000 trainnings (examples), but it can get reduced by the 
     * value of TAX_OF_IMAGES_FROM_FILES.
     */
    public static final int TOT_TRAINNINGS = (int) (50000 * TAX_OF_IMAGES_FROM_FILES);
    /**
     * @see TAX_OF_IMAGES_FROM_FILES
     * TOT_TESTS is the total number of tests. As for CIFAR-10 it 
     * is 10000 examples, but it can get reduced by the value of TAX_OF_IMAGES_FROM_FILES.
     */
    public static final int TOT_TESTS = (int) (10000 * TAX_OF_IMAGES_FROM_FILES);
    /**
     * The total number of pixels in a CIFAR-10 image, 32x32, the size of each image.
     */
    public static final int TOT_PIXELS = 32*32;
    /**
     * The total number of bytes in a CIFAR-10 image, 3*32x32, where 3 bytes are one
     * for each color (red, green and blue, aka RGB) times the size of each image.
     */
    public static final int TOT_BYTES = 3 * TOT_PIXELS;
    /**
     * The size in bytes of each line fo the data files, TOT_BYTES bytes that holds
     * an image plus 1 byte that holds the encoded label that classifies the image. 
     * labels are encoded from 0 to 9, each representing one of the ten classes names
     * of CIFAR-10.
     */
    public static final int CIFAR_LINE = TOT_BYTES +1;

    FileInputStream inputStream;

    /**
     * @see RealMatrix
     * About the Apache Commons Mathematics Library:
     * Commons Math is a library of lightweight, self-contained mathematics and 
     * statistics components addressing the most common problems not available 
     * in the Java programming language or Commons Lang.
     * You can visit to understand and download at http://commons.apache.org/proper/commons-math/.
     * 
     * Several algorithms used in machine-learning are very likely to be defined 
     * as an operation of matrixes.
     * As it is convinient to think and express them in this way, and as Java does 
     * not have spetialized classes to perform matrixes and linear algebra in it's 
     * API, let's assume that the use a traditional Apache Foundation basic library
     * will not hide aspects of the coding we want to teach here, and rather will 
     * contribute to a better understanding of the operations.
     * 
     */
    private final RealMatrix Xtr, Ytr, Xte, Yte;
    private final String[] names;
    private JFrame jFrame;

    /**
     * the constructor method
     * A default constructor (takes no parameters)
     * @throws IOException 
     */
    public Cifar10Utils() throws IOException {
        System.out.println("Cifar10Utils: trying to read " + TOT_TRAINNINGS + " images from example files");
        System.out.println("Cifar10Utils: trying to read " + TOT_TESTS + " images from test file");
        /**
         * org.apache.commons.math3.linear.RealMatrix.Xtr,
         * a matrix with TOT_TRAINNINGS rows and TOT_BYTES columns.
         * 
         * Notice that this matrix is prepared to hold TOT_TRAINNINGS
         * images (50000 if a percentage is not applied), each of
         * size TOT_BYTES (3 x 32 x 32).
         * 
         * Each row of the matrix shall contain the bytes of one trainning image.
        */
        Xtr = new Array2DRowRealMatrix(TOT_TRAINNINGS, TOT_BYTES);
        /**
         * org.apache.commons.math3.linear.RealMatrix.Ytr,
         * a matrix with TOT_TRAINNINGS rows and one column.
         * 
         * Notice that this matrix is prepared to hold TOT_TRAINNINGS
         * encoded labels (50000 if a percentage is not applied), each of
         * value double from 0 to 9 (encoding the 10 label names from CIFAR-10).
         * 
         * Each row of the matrix can contain the label corresponding to the trainning 
         * image of the same row in Xtr matrix.
        */
        Ytr = new Array2DRowRealMatrix(TOT_TRAINNINGS, 1);
        /**
         * org.apache.commons.math3.linear.RealMatrix.Xte,
         * a matrix with TOT_TESTS rows and TOT_BYTES columns.
         * 
         * Notice that this matrix is prepared to hold TOT_TESTS
         * images (10000 if a percentage is not applied), each of
         * size TOT_BYTES (3 x 32 x 32).
         * 
         * Each row of the matrix shall contain the bytes of one test image.
        */
        Xte = new Array2DRowRealMatrix(TOT_TESTS, TOT_BYTES);
        /**
         * org.apache.commons.math3.linear.RealMatrix.Ytr,
         * a matrix with TOT_TRAINNINGS rows and one column.
         * 
         * Notice that this matrix is prepared to hold TOT_TESTS
         * encoded labels (10000 if a percentage is not applied), each of
         * value double from 0d to 9d (encoding the 10 label names from CIFAR-10).
         * 
         * Each row of the matrix can contain the label corresponding to the test 
         * image of the same row in Xtr matrix.
        */
        Yte = new Array2DRowRealMatrix(TOT_TESTS, 1);

        // action begins!

        // retrieve the labels names
        names = readLabelsNames();

        /**
         * The images of the trainning set (roughtly 50000 of them) are stored among
         * 5 files, data_batch_1.bin, data_batch_2.bin, ..., data_batch_5.bin.
         * The images we retrive from the first file should be stored in Xtr beginning
         * at the first row of the matriz (row(o)). But then, the ones from the next
         * file should be stored beginning in the row just after the row in witch 
         * the last image of the first set was stored.
         * 
         * we use rowOffset to indicate the row offset where we must start storing
         * images from a next file, considering that some rows are already in use 
         * storing images read from previous files.
         * 
         * From CIFAR-10 as we know that we read TOT_TRAINNINGS from each of 5 files, 
         * we simply divide TOT_TRAINNINGS by 5.
         * 
         * We use it to control Ytr labels loading offset as well.
         */
        int rowOffset = 0;

        // retrieve the trainning data from files
        readCifar10FileData("./data/data_batch_1.bin", Xtr, Ytr, TOT_TRAINNINGS / 5, rowOffset);
        rowOffset += TOT_TRAINNINGS / 5;
        readCifar10FileData("./data/data_batch_2.bin", Xtr, Ytr, TOT_TRAINNINGS / 5, rowOffset);
        rowOffset += TOT_TRAINNINGS / 5;
        readCifar10FileData("./data/data_batch_3.bin", Xtr, Ytr, TOT_TRAINNINGS / 5, rowOffset);
        rowOffset += TOT_TRAINNINGS / 5;
        readCifar10FileData("./data/data_batch_4.bin", Xtr, Ytr, TOT_TRAINNINGS / 5, rowOffset);
        rowOffset += TOT_TRAINNINGS / 5;
        readCifar10FileData("./data/data_batch_5.bin", Xtr, Ytr, TOT_TRAINNINGS / 5, rowOffset);

        // retrieve the test data from files
        
        rowOffset = 0;
        readCifar10FileData("./data/test_batch.bin", Xte, Yte, TOT_TESTS, rowOffset);

        // For the sake of variaty, when doing some observation, and
        // specially when showing images, we better shuffle them, to decrease the
        // chance of lots of images of same category appearing in the same vicinity.

        shuffleMatrix();
    }

    private String[] readLabelsNames() throws IOException {

        // retrieve the labels names
        String[] namesRetrieval = new String[10];
        for(int i = 0; i<10; i++) {
            namesRetrieval[i] = "";
        }
        String line;
        int index = 0;
        try (
                InputStream fis = new FileInputStream("./data/batches.meta.txt");
                InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
                BufferedReader br = new BufferedReader(isr);) {
            while ((line = br.readLine()) != null && index<10) {
                namesRetrieval[index] = line;
                System.out.println("name[" + index + "] = " + line);
                index++;
            }
        }
        return namesRetrieval;
    }

    final void readCifar10FileData(String fileName, RealMatrix X, RealMatrix Y,
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
        for (int row = 0; row < nImages; row++) {
            label[0] = b[row * CIFAR_LINE] & 0xFF;
            Y.setRow(row + initRow, label);
            for (int col = 1; col < CIFAR_LINE; col++) {
                X.setEntry(row + initRow, col - 1, b[row * CIFAR_LINE + col] & 0xFF);
            }
            System.out.println("row: " + (row + initRow) + " label: " + label[0]);
            /*
            if(row%20==0) {
                displayImage(b);
            }
             */
        }
    }

    private void shuffleMatrix() throws IOException {
        Random random = new Random();
        int index1, index2;
        double[] tmpX, tmpY;
        // shuffle examples
        for (int i = 0; i < TOT_TRAINNINGS / 50; i++) {
            // select 2 random rows
            index1 = random.nextInt(TOT_TRAINNINGS);
            index2 = random.nextInt(TOT_TRAINNINGS);
            // swap rows
            tmpX = Xtr.getRow(index1);
            tmpY = Ytr.getRow(index1);
            Xtr.setRow(index1, Xtr.getRow(index2));
            Ytr.setRow(index1, Ytr.getRow(index2));
            Xtr.setRow(index2, tmpX);
            Ytr.setRow(index2, tmpY);
        }
        // show some trains lines
        //double[] util = new double[1]; 
        //for (int k = 0; k < 10 * 18; k++) {
        //    util = Ytr.getRow(k);
        //    displayImage(Xtr.getRow(k), util[0]);
        //}

        // shuffle tests
        for (int i = 0; i < TOT_TESTS / 10; i++) {
            // select 2 random rows
            index1 = random.nextInt(TOT_TESTS);
            index2 = random.nextInt(TOT_TESTS);
            // swap rows
            tmpX = Xte.getRow(index1);
            tmpY = Yte.getRow(index1);
            Xte.setRow(index1, Xte.getRow(index2));
            Yte.setRow(index1, Yte.getRow(index2));
            Xte.setRow(index2, tmpX);
            Yte.setRow(index2, tmpY);
        }
    }

    /**
     * returns a JFrame object.
     * 
     * The first message to the this method, will probably find jFrame = null,
     * then it creates a new JFrame object and makes jFram points to it.
     * In subsequent calls, the object already exists, so, it is returned without
     * the creation of a new one.
     * 
     * @return a JFrame object
     */
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
     *
     * @param b here the values comes from the file, so its size is 3073 bytes
     * note that we should add 1 to create the RGB color (we add 1 when we must
     * discount the label in buffers of length 3073).
     * @param index
     * @throws IOException
     */

    public void displayImage(byte[] b, double index) throws IOException {
        // just display an image
        System.out.println("Displaying image from b[] lenght: " + b.length);
        BufferedImage image = new BufferedImage(32, 32, BufferedImage.TYPE_INT_RGB);
        for (int row = 0; row < 32; row++) {
            for (int col = 0; col < 32; col++) {
                Color color = new Color(
                        b[1 + TOT_PIXELS * 0 + row * 32 + col] & 0xFF,
                        b[1 + TOT_PIXELS * 1 + row * 32 + col] & 0xFF,
                        b[1 + TOT_PIXELS * 2 + row * 32 + col] & 0xFF);
                image.setRGB(col, row, color.getRGB());
            }
        }
        jFrame = getjFrame();
        JLabel label = new JLabel(new ImageIcon(image));
        label.setText(names[(int) index]);
        label.setHorizontalTextPosition(JLabel.CENTER);
        label.setVerticalTextPosition(JLabel.BOTTOM);
        label.setFont(new Font(label.getFont().getName(), Font.PLAIN, 6));
        jFrame.getContentPane().add(label);
        //jFrame.getContentPane().add(new JLabel(new ImageIcon(image)));
        jFrame.setVisible(true);
        boolean result = ImageIO.write(image, "jpeg", new FileOutputStream("./out.jpg"));
        if (!result) {
            System.err.println("failed");
        }
    }

    /**
     * Displays one image
     *
     * @param b here the values comes from the matrix, so its size is 3072 bytes
     * note that we should not add 1 to create the RGB color (we add 1 when we
     * must discount the label in buffers of length 3073).
     * @param index
     * @throws IOException
     */
    public void displayImage(double[] b, double index) throws IOException {
        // just display an image
        System.out.println("Displaying image from b[] lenght: " + b.length);
        BufferedImage image = new BufferedImage(32, 32, BufferedImage.TYPE_INT_RGB);
        for (int row = 0; row < 32; row++) {
            for (int col = 0; col < 32; col++) {
                Color color = new Color(
                        ((byte) b[TOT_PIXELS * 0 + row * 32 + col]) & 0xFF,
                        ((byte) b[TOT_PIXELS * 1 + row * 32 + col]) & 0xFF,
                        ((byte) b[TOT_PIXELS * 2 + row * 32 + col]) & 0xFF);
                image.setRGB(col, row, color.getRGB());
            }
        }
        jFrame = getjFrame();
        JLabel label = new JLabel(new ImageIcon(image));
        label.setText(names[(int) index]);
        label.setHorizontalTextPosition(JLabel.CENTER);
        label.setVerticalTextPosition(JLabel.BOTTOM);
        label.setFont(new Font(label.getFont().getName(), Font.PLAIN, 6));
        jFrame.getContentPane().add(label);
        jFrame.setVisible(true);
        boolean result = ImageIO.write(image, "jpeg", new FileOutputStream("./out.jpg"));
        if (!result) {
            System.err.println("failed");
        }
    }

    /**
     * getter (access method)
     * @return Xtr
     */
    public RealMatrix getXtr() {
        return Xtr;
    }

    /**
     * getter (access method)
     * @return Ytr
     */
    public RealMatrix getYtr() {
        return Ytr;
    }

    /**
     * getter (access method)
     * @return Xte
     */
    public RealMatrix getXte() {
        return Xte;
    }

    /**
     * getter (access method)
     * @return Yte
     */
    public RealMatrix getYte() {
        return Yte;
    }
}
