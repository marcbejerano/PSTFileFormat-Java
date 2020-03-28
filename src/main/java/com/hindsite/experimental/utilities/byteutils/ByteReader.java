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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * @author Tal Aloni <tal@kmrom.com>
 * @author Marc Bejerano <marcbejerano@gmail.com>
 */
public class ByteReader {

    public static byte getByte(byte[] buffer, int offset) {
        return buffer[offset];
    }

    public static byte[] getBytes(byte[] buffer, int offset, int length) {
        byte[] result = new byte[length];
        System.arraycopy(buffer, offset, result, 0, length);
        return result;
    }

    /// <summary>
    /// Will return the ANSI string stored in the buffer
    /// </summary>
    public static String getAnsiString(byte[] buffer, int offset, int count) {
        // ASCIIEncoding.ASCII.GetString will convert some values to '?' (byte value of 63)
        // Any codepage will do, but the only one that Mono supports is 28591.
        return new String(buffer, offset, count, Charset.forName("ansi"));
    }

    public static String getUTF16String(byte[] buffer, int offset, int numberOfCharacters) {
        int numberOfBytes = numberOfCharacters * 2;
        return new String(buffer, offset, numberOfBytes, Charset.forName("utf-16"));
    }

    public static String getNullTerminatedAnsiString(byte[] buffer, int offset) {
        StringBuilder builder = new StringBuilder();
        char c = (char) ByteReader.getByte(buffer, offset);
        while (c != '\0') {
            builder.append(c);
            offset++;
            c = (char) ByteReader.getByte(buffer, offset);
        }
        return builder.toString();
    }

    public static String getNullTerminatedUTF16String(byte[] buffer, int offset) {
        StringBuilder builder = new StringBuilder();
        char c = (char) LittleEndianConverter.getUInt16(buffer, offset);
        while (c != 0) {
            builder.append(c);
            offset += 2;
            c = (char) LittleEndianConverter.getUInt16(buffer, offset);
        }
        return builder.toString();
    }

    public static byte[] getBytes(InputStream stream, int count) throws IOException {
        ByteArrayOutputStream temp = new ByteArrayOutputStream();
        ByteUtils.copy(stream, temp, count);
        return temp.toByteArray();
    }

    /// <summary>
    /// Return all bytes from current stream position to the end of the stream
    /// </summary>
    public static byte[] getAllBytes(InputStream stream) throws IOException {
        ByteArrayOutputStream temp = new ByteArrayOutputStream();
        ByteUtils.copy(stream, temp);
        return temp.toByteArray();
    }
}
