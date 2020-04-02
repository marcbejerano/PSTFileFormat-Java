/*
 * Copyright © 2007 Free Software Foundation, Inc. <https://fsf.org/>
 *
 * Everyone is permitted to copy and distribute verbatim copies of this license
 * document, but changing it is not allowed.
 *
 * This version of the GNU Lesser General Public License incorporates the terms
 * and conditions of version 3 of the GNU General Public License, supplemented
 * by the additional permissions listed below.
 */
package com.hindsite.pst.utils;

import java.util.Arrays;

/**
 * @author Marc Bejerano <marcbejerano@gmail.com>
 */
public class ArrayUtils {

    public static void dumpCompareArrays(byte[] a, byte[] b) {
        System.out.print("A: ");
        for (int n = 0; n < a.length; n++) {
            System.out.print(String.format("%02x", a[n]));
        }
        System.out.println("");
        System.out.print("B: ");
        for (int n = 0; n < b.length; n++) {
            System.out.print(String.format("%02x", b[n]));
        }
        System.out.println("");
    }

    public static void dumpArray(byte[] a, int w) {
        int wdelta = w;
        System.out.println(String.format("Dumping %d bytes", a.length));
        char[] line = new char[5 + w * 3];
        Arrays.fill(line, '-');
        System.out.println(new String(line));
        System.out.print("0000: ");
        for (int i = 0; i < a.length; ++i) {
            System.out.print(String.format("%02x", a[i] & 0xFF));
            if (wdelta > 0) {
                System.out.print(" ");
            }
            if (--wdelta == 0) {
                System.out.println();
                System.out.print(String.format("%04x: ", i + 1));
                wdelta = w;
            }
        }
        System.out.println();
    }
}
