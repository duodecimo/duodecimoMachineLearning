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
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;
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
 * The CIFAR-10 dataset consists of 60000 32x32 pixels colour images in 10 classes,
 * with 6000 images per class. There are 50000 training images and 10000 test images.
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
 * 
 * Portuguese version:
 * 
 * Cifar10Utils é uma classe que oferece métodos úteis para ler e processar
 * imagens da base de dados CIFAR_10.
 * 
 * O que são CIFAR-10 e CIFAR-100?
 * A CIFAR-10 e a CIFAR-100 são subvonjuntos rotulados de um conjunto de 80 milhões
 * de pequenas imagens. Foram coletadas por Alex Krizhevsky, Vinod Nair, e Geoffrey Hinton.
 * visite: https://www.cs.toronto.edu/~kriz/cifar.html
 * 
 * O conjunto CIFAR-10 consiste de 60000 imagens coloridas de 32x32 pixels em 10 
 * classes, com 6000 imagens por classe. Existem 50000 imagens de treino e 10000 
 * imagens de teste.
 * O conjunto está dividido em 5 arquivos de lotes de treino e um lote de teste, 
 * cada um com 10000 imagens. O lote de teste contém exatamente 1000 imagens
 * aleatoriamente selecionadas de cada classe.
 * Os lotes de treino contém o resto das  imagens em ordem aleatória,
 * mas alguns lotes de treino podem conter mais imagens de uma classe do que de
 * alguma das outras.
 * Juntos, os lotes de treino contém exatamente 5000 imagens de cada classe. 
 * 
 * Cifar10Utils é destinada a trabalhar com a versão binária da CIFAR-10, baixada
 * a partir de https://www.cs.toronto.edu/~kriz/cifar.html.
 * 
 * A versão binária contém os arquivos data_batch_1.bin, data_batch_2.bin, ..., 
 * data_batch_5.bin, bem como o test_batch.bin. Cada um destes arquivos é formatado
 * como segue:
 * <1 x rótulo><3072 x pixel> ...
 * <1 x rótulo><3072 x pixel>
 * Em outras palavras, o primeiro byte é o rótulo da primeira imagem, um número
 * entre 0-9. Os próximos 3072 bytes são os valores dos pixels da imagem.
 * Os primeiros 1024 bytes são os valores do canal vermelho, os próximos 1024 o
 * verde, e os últimos 1024 o azul. Os valores são armazenados em ordem de
 * maior-linha, portanto os primeiros 32 bytes são osvalores do canal vermelho da
 * primeira linha da imagem.
 * 
 * Cada arquivo contém 10000 destas imagens de 3073-byte, mas não existe nada
 * delimitando as linhas. Portanto, cada arquivo deve ter exatamente o tamanho
 * de 30730000 bytes. 
 * 
 * Existe um outro arquivo, chamado batches.meta.txt. É um arquivo do tipo ASCII
 * que mapeia rótulos numéricos na faixa de 0-9 para nomes de classes significativos.
 * É simplesmente uma lista dos 10 nomes de classe, um por linha. 
 * O nome da classe na linha i corresponde ao rótulo numérico i.
 */
public class Cifar10Utils {

    /**
     * This value will be used as a tax to decide the percentage of the amount of
     * images that will be retrieved from each CIFAR-10 data file.
     * So, a value of 0.1f will retrive only 10% of the images, as a value of 
     * 1.0f will retrieve 100% of the images.
     * To get thrustfull test results comparable to other results made public
     * by people that used CIFAR-10 too, one should use 100% of the data.
     * This variable is here in order to facilitate using less data to check
     * code funcionality, and as a extreme resource to run in slow and lower
     * memory machines reducing processing time and effort.
     * 
     * Portuguese version:
     * 
     * Este valor será utilizado como uma taxa para determinar o percentual do
     * numero de imagens a serem lidas de cada arquivo de dados da CIFAR-10.
     * Então, um valor de 0.1f irá ler apenas 10% das imagens, já um valor de
     * 1.0f vai ler 100% das imagens.
     * Para a obtenção de testes com resultados confiáveis, comparáveis a 
     * resultados publicados por outras pessoas que usaram o CIFAR-10 também,
     * deve-se utilizar 100% dos dados.
     * A finalidade desta variável é facilitar o uso de menor quantidade de dados
     * para verificar a execução de alterações no código, e como um recurso
     * extremo para execução em máquinas lentas, e memória insuficiente, reduzindo
     * o tempo de execução e uso de memória.
     */
    public static final float TAX_OF_IMAGES_FROM_FILES = 0.1f;

    /**
     * @see TAX_OF_IMAGES_FROM_FILES
     * 
     * TOT_TRAINNINGS is the total number of trainning images (examples). As for 
     * CIFAR-10 it is 50000 trainnings (examples), but it can get reduced by the 
     * value of the variable TAX_OF_IMAGES_FROM_FILES.
     * 
     * Portuguese version:
     * 
     * TOT_TRAINNINGS é o número total imagens de treino (exemplos). No caso da
     * CIFAR-10 são 50000 treinamentos (exemplos), mas o valor pode ser reduzido
     * pelo valor atribuido a variável TAX_OF_IMAGES_FROM_FILES.
     */
    public static final int TOT_TRAINNINGS = (int) (50000 * TAX_OF_IMAGES_FROM_FILES);

    /**
     * @see TAX_OF_IMAGES_FROM_FILES
     * TOT_TESTS is the total number of test images. As for CIFAR-10 it 
     * is 10000 examples, but it can get reduced by the value of the variable
     * TAX_OF_IMAGES_FROM_FILES.
     * 
     * Portuguese version:
     * 
     * TOT_TESTS é o numero total de imagens de teste. No caso da CIFAR-10 são 
     * 10000 examplos, mas a quantidade pode ser reduzida pelo valor da variável
     * TAX_OF_IMAGES_FROM_FILES.
     */
    public static final int TOT_TESTS = (int) (10000 * TAX_OF_IMAGES_FROM_FILES);

    /**
     * The total number of pixels in a CIFAR-10 image, 32x32, the size of each image.
     * 
     * Portuguese version:
     * 
     * O número total de pixels em uma imagem da CIFAR-10, 32x32, o tamanho de
     * cada imagem.
     */
    public static final int TOT_PIXELS = 32*32;

    /**
     * The total number of bytes in a CIFAR-10 image, 3*32x32, where 3 bytes are
     * each of one of the RGB colors intensity value, times the size in pixels
     * of each image.
     * 
     * Portuguese version:
     * 
     * O número total de bytes em uma imagem da CIFAR-10, 3*32x32, where 3 bytes
     * são cada um dos valores da intensidade das cores RGB (red, green, blue, ou,
     * vermelho, verde e azul) vezes o tamanho em pixels de cada imagem.
     */
    public static final int TOT_BYTES = 3 * TOT_PIXELS;

    /**
     * The size in bytes of each line of the data files, TOT_BYTES bytes, that 
     * holds an image plus 1 byte that holds the value of the encoded label that 
     * classifies the image. 
     * labels are encoded with values from 0 to 9, each representing one of the
     * ten classes names used in CIFAR-10.
     * 
     * Portuguese version:
     * 
     * O tamanho em bytes de cada linha dos arquivos de imagens, TOT_BYTES bytes,
     * que contém uma imagem mais 1 byte que contém o valor labeldo rótulo 
     * codificado que classifica a imagem.
     * rótulos são codificados com valores de 0 a 9, cada um, representando o nome
     * de uma das dez classes utilizadas na CIFAR-10.
     */
    public static final int CIFAR_LINE = TOT_BYTES +1;

    FileInputStream inputStream;

    /**
     * @see RealMatrix
     * About the Apache Commons Mathematics Library:
     * Commons Math is a library of lightweight, self-contained mathematics and 
     * statistics components addressing the most common problems not available 
     * in the Java programming language or Commons Lang.
     * You can visit to understand and download at 
     * http://commons.apache.org/proper/commons-math/.
     * 
     * Several algorithms used in machine-learning are very likely to be defined 
     * as an operation of matrixes.
     * As it is convinient to think and express them in this way, and as Java does 
     * not have a dedicated API to perform matrixes and linear algebra operations,
     * let's assume that the use a traditional Apache Foundation basic library
     * will not hide aspects of the coding we want to teach here, and rather will 
     * contribute to a better understanding of the operations.
     * 
     * Portuguese version:
     * 
     * Sobre a biblioteca Apache Commons Mathematics:
     * Commons Math é uma biblioteca de componentes estatíticos peso leve e 
     * auto contidos, endereçando os problemas mais comuns não disponíveis na
     * linguagem de programação Java ou em Commons Lang.
     * Você pode visitar e baixar de
     * http://commons.apache.org/proper/commons-math/.
     * 
     * Vários algoritmos utilizados em aprendizagem de máquina são normalmente 
     * definidos através de operações em matrizes.
     * Como é conveniente pensá-los e expressá-los desta forma, e como Java não
     * possui uma API dedicada a executar operações em matrizes e algebra linear,
     * Vamos assumir que utilizar uma biblioteca da tradicional Fundação Apache
     * não vai ocultar aspectos do código que desejamos ensinar aquí, na verdade
     * vai contribuir para melhor entendimento das operações.
     */
    private final RealMatrix Xtr, Ytr, Xte, Yte;

    private final String[] names;
    private JFrame jFrame;
    private static final Logger LOGGER = Logger.getGlobal();
    private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("ml/Bundle");

    /**
     * the constructor method
     * A default constructor (takes no parameters)
     * 
     * Portuguese version:
     * 
     * o método construtor
     * um construtor padrão (não recebe parametros)
     * 
     * @throws IOException 
     */
    public Cifar10Utils() throws IOException {
        LOGGER.setLevel(Level.INFO);
        LOGGER.log(Level.INFO, BUNDLE.getString("CIFAR10UTILS: READING {0} IMAGES FROM EXAMPLE FILES"), TOT_TRAINNINGS);
        LOGGER.log(Level.INFO, BUNDLE.getString("CIFAR10UTILS: READ {0} IMAGES FROM TEST FILE"), TOT_TESTS);

        /**
         * org.apache.commons.math3.linear.RealMatrix.Xtr,
         * a matrix with TOT_TRAINNINGS rows and TOT_BYTES columns.
         * 
         * Notice that this matrix is prepared to hold TOT_TRAINNINGS
         * images (50000 if a reduction percentage is not applied), each of
         * size TOT_BYTES (3 x 32 x 32 = 3072).
         * 
         * Each row of the matrix holds bytes of one trainning image.
         * 
         * Portuguese version:
         * 
         * org.apache.commons.math3.linear.RealMatrix.Xtr,
         * uma mariz com TOT_TRAINNINGS linhas e TOT_BYTES colunas.
         * 
         * Note que esta matriz estra preparada para conter TOT_TRAINNINGS
         * imagens (50000 se um percentual redutor não for utilizado), cada um 
         * de tamanho TOT_BYTES (3 x 32 x 32 = 3072).
         * 
         * Cada linha da matriz contém os bytes de uma imagem de treino.
         */
        Xtr = new Array2DRowRealMatrix(TOT_TRAINNINGS, TOT_BYTES);

        /**
         * org.apache.commons.math3.linear.RealMatrix.Ytr,
         * a matrix with TOT_TRAINNINGS rows and one column.
         * 
         * Notice that this matrix is prepared to hold TOT_TRAINNINGS
         * encoded labels (50000 if a reduction percentage is not applied), each of
         * value double from 0 to 9 (encoding the 10 label names from CIFAR-10).
         * 
         * Each row of the matrix holds the label corresponding to the trainning 
         * image of the same row in Xtr matrix.
         * 
         * Portuguese version:
         * 
         * org.apache.commons.math3.linear.RealMatrix.Ytr,
         * uma matriz com TOT_TRAINNINGS linhas e uma coluna.
         * 
         * Note que esta matriz está preparada para conter TOT_TRAINNINGS
         * rótulos codificados (50000 se uma percentagem redutora não for aplicada),
         * cada um com um valor double de 0 a 9 (codificando os nomes dos 10
         * rótulos da CIFAR-10).
         * 
         * Cada linha da matriz contém o rótulo correspondente a imagem de
         * treino de mesma linha na matriz Xtr.
        */
        Ytr = new Array2DRowRealMatrix(TOT_TRAINNINGS, 1);
 
        /**
         * org.apache.commons.math3.linear.RealMatrix.Xte,
         * a matrix with TOT_TESTS rows and TOT_BYTES columns.
         * 
         * Notice that this matrix is prepared to hold TOT_TESTS
         * images (10000 if a reduction percentage is not applied), each of
         * size TOT_BYTES (3 x 32 x 32 = 3072).
         * 
         * Each row of the matrix holds the bytes of one test image.
         * 
         * Portuguese version:
         * 
         * org.apache.commons.math3.linear.RealMatrix.Xte,
         * uma matriz com TOT_TESTS linhas e TOT_BYTES colunas.
         * 
         * Note que esta matriz está preparada para conter TOT_TESTS
         * imagens (10000 se um percentual redutor não for aplicado), cada uma
         * com o tamanho TOT_BYTES (3 x 32 x 32 = 3072).
         * 
         * Cada linha da matriz contém os bytes de uma imagem de teste.
        */
        Xte = new Array2DRowRealMatrix(TOT_TESTS, TOT_BYTES);

        /**
         * org.apache.commons.math3.linear.RealMatrix.Ytr,
         * a matrix with TOT_TRAINNINGS rows and one column.
         * 
         * Notice that this matrix is prepared to hold TOT_TESTS
         * encoded labels (10000 if a reduction percentage is not applied), each of
         * value double from 0d to 9d (encoding the 10 label names from CIFAR-10).
         * 
         * Each row of the matrix holds the label corresponding to the test 
         * image of the same row in Xtr matrix.
         * 
         * Portuguese version:
         * 
         * org.apache.commons.math3.linear.RealMatrix.Ytr,
         * uma matriz com TOT_TRAINNINGS linhas e uma coluna.
         * 
         * Note que esta matriz está preparada para conter TOT_TESTS
         * rótulos codificados (10000 se um percentual redutor não for aplicado),
         * cada um com valor double de 0d a 9d (codificando os nomes dos rótulos
         * da CIFAR-10).
         * 
         * Cada linha contém o rótulo correspondente a imagem de teste de mesma
         * linha ma matriz Xtr.
        */
        Yte = new Array2DRowRealMatrix(TOT_TESTS, 1);

        // convention: small tips will bring a trailling portuguese version
        // enclosed between parenthesis without further explanation. (convenção:
        // pequenos comentários trarão ao final uma versão em português entre
        // parentesis sem subsequentes avisos).

        // Action! (Ação!)

        // retrieve the labels names (leia os nomes dos rótulos)
        names = readLabelsNames();

        /**
         * The images of the trainning set (roughtly 50000 of them) are stored among
         * 5 files, data_batch_1.bin, data_batch_2.bin, ..., data_batch_5.bin.
         * The images we retrive from the first file should be stored in Xtr beginning
         * at the first row of the matriz (row(0)). But then, the ones from the next
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
         * 
         * Portuguese version:
         * 
         * As imagens do conjunto de treino (chegando a 50000 delas) são distribuidas
         * entre 5 arquivos, data_batch_1.bin, data_batch_2.bin, ..., data_batch_5.bin.
         * As imagens lidas do primeiro arquivo devem ser armazenadas em Xtr começando
         * na primeira linha da matriz (row(0)). Então, as do próximo arquivo
         * devem ser armazenadas começando a partir da linha imediatamente após a linha
         * onde a última imagem do lote anterior foi armazenada.
         * 
         * utilizamos rowOffset para indicar o espaçamento (offset) onde precisamos
         * começar o armazenamento de imagens de um próximo lote de arquivo, 
         * considerando que algumas linhas já estão em uso, com imagens armazenadas
         * de outros arquivos préviamente lidos.
         * 
         * De CIFAR-10 como sabemos lemos TOT_TRAINNINGS de cada um dos 5 arquivos, 
         * vamos simplesmente dividir TOT_TRAINNINGS por 5.
         * 
         * Utilizamos o mesmo valor para controlar o espaçamento da leitura de
         * rótulos também.
         */
        int rowOffset = 0;

        // retrieve the trainning data from files (leia os treinos dos arquivos de dados)
        readCifar10FileData(BUNDLE.getString("./DATA/DATA_BATCH_1.BIN"), Xtr, Ytr, TOT_TRAINNINGS / 5, rowOffset);
        rowOffset += TOT_TRAINNINGS / 5;
        readCifar10FileData(BUNDLE.getString("./DATA/DATA_BATCH_2.BIN"), Xtr, Ytr, TOT_TRAINNINGS / 5, rowOffset);
        rowOffset += TOT_TRAINNINGS / 5;
        readCifar10FileData(BUNDLE.getString("./DATA/DATA_BATCH_3.BIN"), Xtr, Ytr, TOT_TRAINNINGS / 5, rowOffset);
        rowOffset += TOT_TRAINNINGS / 5;
        readCifar10FileData(BUNDLE.getString("./DATA/DATA_BATCH_4.BIN"), Xtr, Ytr, TOT_TRAINNINGS / 5, rowOffset);
        rowOffset += TOT_TRAINNINGS / 5;
        readCifar10FileData(BUNDLE.getString("./DATA/DATA_BATCH_5.BIN"), Xtr, Ytr, TOT_TRAINNINGS / 5, rowOffset);

        // reset (zere)
        rowOffset = 0;

        // retrieve the test data from file (leia os dados de teste do arquivo)
        readCifar10FileData(BUNDLE.getString("./DATA/TEST_BATCH.BIN"), Xte, Yte, TOT_TESTS, rowOffset);

        // Specially when using reduced set, for the sake of variaty, when doing 
        // some observations, and even specially when showing images, we may try to
        // shuffle them, to decrease the chance of lots of images of same category 
        // appearing in the same vicinity.
        // (Especialmente quando utilizarmos conjuntos reduzidos, para fazer alguma
        // observação, e mais ainda especialmente quando formos mostar algumas imagens,
        // podemos tentar embaralhar, para diminuir as chances de lotes de imagens
        // da mesma categoria aparecerem frquentemente na mesma vizinhança).

        // to shuffle uncomment the line below (descomente a linha abaixo para embaralhar)
        //shuffleMatrix();
    }

    private String[] readLabelsNames() throws IOException {

        // retrieve the labels names (leia os nomes dos rótulos)
        String[] namesRetrieval = new String[10];
        for(int i = 0; i<10; i++) {
            namesRetrieval[i] = "";
        }
        String line;
        int index = 0;
        try (
                InputStream fis = new FileInputStream(BUNDLE.getString("./DATA/BATCHES.META.TXT"));
                InputStreamReader isr = new InputStreamReader(fis, Charset.forName(BUNDLE.getString("UTF-8")));
                BufferedReader br = new BufferedReader(isr);) {
            LOGGER.log(Level.FINE, BUNDLE.getString("LIST OF LABELS:"));
            while ((line = br.readLine()) != null && index<10) {
                namesRetrieval[index] = line;
                LOGGER.log(Level.FINE, BUNDLE.getString("LABEL[{0}] = {1}"), new Object[]{index, line});
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
        int readings;
        readings = inputStream.read(b);
        LOGGER.log(Level.FINE, BUNDLE.getString("FILE: {0} READINGS: {1} ({2} * {3})"), 
                new Object[]{fileName, readings, nImages, readings / nImages});
        LOGGER.log(Level.FINER, BUNDLE.getString("SHOULD READ: {0}"), nImages * CIFAR_LINE);
        for (int row = 0; row < nImages; row++) {
            label[0] = b[row * CIFAR_LINE] & 0xFF;
            Y.setRow(row + initRow, label);
            for (int col = 1; col < CIFAR_LINE; col++) {
                X.setEntry(row + initRow, col - 1, b[row * CIFAR_LINE + col] & 0xFF);
            }
            LOGGER.log(Level.FINER, BUNDLE.getString("ROW: {0} LABEL: {1}"), new Object[]{row + initRow, label[0]});
        }
    }

    private void shuffleMatrix() throws IOException {
        Random random = new Random();
        int index1, index2;
        double[] tmpX, tmpY;
        // shuffle examples (embaralhe os exemplos)
        for (int i = 0; i < TOT_TRAINNINGS / 50; i++) {
            // select 2 random rows (selecione duas linhas quaisquer)
            index1 = random.nextInt(TOT_TRAINNINGS);
            index2 = random.nextInt(TOT_TRAINNINGS);
            // swap rows (troque as linhas)
            tmpX = Xtr.getRow(index1);
            tmpY = Ytr.getRow(index1);
            Xtr.setRow(index1, Xtr.getRow(index2));
            Ytr.setRow(index1, Ytr.getRow(index2));
            Xtr.setRow(index2, tmpX);
            Ytr.setRow(index2, tmpY);
        }

        // shuffle tests (embaralhe os testes)
        for (int i = 0; i < TOT_TESTS / 10; i++) {
            // select 2 random rows (seleciona aleatoriamente duas linhas)
            index1 = random.nextInt(TOT_TESTS);
            index2 = random.nextInt(TOT_TESTS);
            // swap rows (troque as linhas)
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
     * then it creates a new JFrame object and makes the identifyer jFrame points to it.
     * In subsequent calls, the object already exists, so, it is returned without
     * the creation of a new one.
     * 
     * @return a JFrame object
     * 
     * Portuguese version:
     * 
     * retorna um objeto JFrame.
     * 
     * A primeira mensagem para este método, vai provávlemente encontrar jFrame = null,
     * então ele cria um novo objeto JFrame e faz com que o identificador jFrame aponte para ele.
     * Nas chamadas subsequentes, o objeto já existe, então é retornado sem a 
     * criação de um novo.     */
    public JFrame getjFrame() {
        if (jFrame == null) {
            jFrame = new JFrame(BUNDLE.getString("IMAGE DISPLAY"));
            jFrame.setLayout(new FlowLayout());
            jFrame.setBounds(100, 100, 680, 630);
            jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        }
        return jFrame;
    }

    /**
     * Displays one image
     *
     * @param b here the values comes from the file, so its size is 3073 bytes
     * note that we should add 1 to create the RGB color (we add 1 when we must
     * discount the label in buffers of length 3073).
     * 
     * @param index
     * 
     * @throws IOException
     * 
     * Portuguese version:
     * 
     * Mostra uma imagem
     *
     * aquí os valores vêm do arquivo, então o tamanho é de 3073 bytes
     * note que devemos adicionar 1 para criar o a cor RGB (adicionamos 1 para
     * discontar o label em buffers de largura 3073).
     */
    public void displayImage(byte[] b, double index) throws IOException {
        // just display an image
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
        jFrame.setVisible(true);
        boolean result = ImageIO.write(image, BUNDLE.getString("JPEG"), new FileOutputStream(BUNDLE.getString("./OUT.JPG")));
        if (!result) {
            LOGGER.log(Level.WARNING, BUNDLE.getString("FAIL CONVERTING IMAGE."));
        }
    }

    /**
     * Displays one image
     *
     * @param b here the values comes from the matrix, so its size is 3072 bytes
     * note that we should not add 1 to create the RGB color (we add 1 when we
     * must discount the label in buffers of length 3073).
     * 
     * @param index
     * 
     * @throws IOException
     * 
     * Portuguese version:
     * 
     * Displays one image
     *
     * @param b aquí os dados vêm da matriz, portanto o tamanho é de 3072 bytes
     * note que não devemos adicionar 1 para criar a cor RGB (adicionamos 1 quando é
     * preciso descontar o rótulo em buffers de largura 3073).
     */
    public void displayImage(double[] b, double index) throws IOException {
        // just display an image
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
        boolean result = ImageIO.write(image, BUNDLE.getString("JPEG"), new FileOutputStream(BUNDLE.getString("./OUT.JPG")));
        if (!result) {
            LOGGER.log(Level.WARNING, BUNDLE.getString("FAIL CONVERTING IMAGE."));
        }
    }

    /**
     * getter access method (método de acesso)
     * @return Xtr
     */
    public RealMatrix getXtr() {
        return Xtr;
    }

    /**
     * getter access method (método de acesso)
     * @return Ytr
     */
    public RealMatrix getYtr() {
        return Ytr;
    }

    /**
     * getter access method (método de acesso)
     * @return Xte
     */
    public RealMatrix getXte() {
        return Xte;
    }

    /**
     * getter access method (método de acesso)
     * @return Yte
     */
    public RealMatrix getYte() {
        return Yte;
    }

    public float getLoadPercentual() {
        return (50000 * TAX_OF_IMAGES_FROM_FILES * 100);
    }

    public int getTotalOfTrainnings() {
        return TOT_TRAINNINGS;
    }

    public int getTotalOfTests() {
        return TOT_TESTS;
    }

    public int getTotalOfPixels() {
        return TOT_PIXELS;
    }

    public int getTotalOfBytes() {
        return TOT_BYTES;
    }
    public String[] getNames() {
        return names;
    }
}
