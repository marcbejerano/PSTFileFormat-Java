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
package com.hindsite.experimental.utilities.byteutils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Tal Aloni <tal@kmrom.com>
 * @author Marc Bejerano <marcbejerano@gmail.com>
 */
public class ByteUtils {

    public static byte[] concatenate(byte[] a, byte[] b) {
        byte[] result = new byte[a.length + b.length];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }

    public static boolean isEqual(byte[] array1, byte[] array2) {
        if (array1.length != array2.length) {
            return false;
        }

        for (int index = 0; index < array1.length; index++) {
            if (array1[index] != array2[index]) {
                return false;
            }
        }

        return true;
    }

    public static byte[] xor(byte[] array1, byte[] array2) throws Exception {
        if (array1.length == array2.length) {
            return xor(array1, 0, array2, 0, array1.length);
        } else {
            throw new IllegalArgumentException("Arrays must be of equal length");
        }
    }

    public static byte[] xor(byte[] array1, int offset1, byte[] array2, int offset2, int length) throws Exception {
        if (offset1 + length <= array1.length && offset2 + length <= array2.length) {
            byte[] result = new byte[length];
            for (int index = 0; index < length; index++) {
                result[index] = (byte) (array1[offset1 + index] ^ array2[offset2 + index]);
            }
            return result;
        } else {
            throw new Exception("Bounds are out of range");
        }
    }

    public static long copy(InputStream input, OutputStream output) throws IOException {
        // input may not support seeking, so don't use input.Position
        return copy(input, output, Integer.MAX_VALUE);
    }

    public static long copy(InputStream input, OutputStream output, long count) throws IOException {
        final int MaxBufferSize = 1048576; // 1 MB
        int bufferSize = (int) Math.min(MaxBufferSize, count);
        byte[] buffer = new byte[bufferSize];
        long totalBytesRead = 0;
        while (totalBytesRead < count) {
            int numberOfBytesToRead = (int) Math.min(bufferSize, count - totalBytesRead);
            int bytesRead = input.read(buffer, 0, numberOfBytesToRead);
            totalBytesRead += bytesRead;
            output.write(buffer, 0, bytesRead);
            if (bytesRead == 0) // no more bytes to read from input stream
            {
                return totalBytesRead;
            }
        }
        return totalBytesRead;
    }
}
