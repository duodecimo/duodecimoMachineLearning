/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.matrix;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

/**
 *
 * @author duo
 */
public class Display {
    public static void displayRealMatrix(String title, String javaCode, String metadata, RealMatrix M) {
        System.out.println(java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("sandbox/Bundle").getString("TASK: {0}"), new Object[]{title}));
        System.out.println(java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("sandbox/Bundle").getString("CODE: {0}"), new Object[]{javaCode}));
        displayRealMatrix(metadata, M);
    }

    public static void displayRealMatrix(String title, String metadata, RealMatrix M) {
        System.out.println(title);
        displayRealMatrix(metadata, M);
    }

    public static void displayRealMatrix(String metadata, RealMatrix M) {
        System.out.println(metadata + java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("sandbox/Bundle").getString(" ({0} X {1})"), new Object[]{M.getRowDimension(), M.getColumnDimension()}));
        displayRealMatrix(M);
    }

    public static void displayRealMatrix(RealMatrix M) {
        for (int l = 0; l < M.getRowDimension(); l++) {
            System.out.println("");
            for (int c = 0; c < M.getColumnDimension(); c++) {
                System.out.print(String.format(" %14.10f", M.getEntry(l, c)));
            }
        }
        System.out.println(java.util.ResourceBundle.getBundle("sandbox/Bundle").getString("\\NEWLINE"));
    }

    public static void displayRealVector(String title, String javaCode, String metadata, RealVector v) {
        System.out.println(java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("sandbox/Bundle").getString("TASK: {0}"), new Object[]{title}));
        System.out.println(java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("sandbox/Bundle").getString("CODE: {0}"), new Object[]{javaCode}));
        displayRealVector(metadata, v);
    }

    public static void displayRealVector(String title, String metadata, RealVector v) {
        System.out.println(title);
        displayRealVector(metadata, v);
    }

    public static void displayRealVector(String metadata, RealVector v) {
        System.out.println(metadata + java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("sandbox/Bundle").getString(" ( DIMENSION = {0})"), new Object[]{v.getDimension()}));
        displayRealVector(v);
    }

    public static void displayRealVector(RealVector v) {
        for (int l = 0; l < v.getDimension(); l++) {
            System.out.println(String.format(java.util.ResourceBundle.getBundle("sandbox/Bundle").getString(" %8.2F"), v.getEntry(l)));
        }
        System.out.println(java.util.ResourceBundle.getBundle("sandbox/Bundle").getString("\\NEWLINE"));
    }
    
}
