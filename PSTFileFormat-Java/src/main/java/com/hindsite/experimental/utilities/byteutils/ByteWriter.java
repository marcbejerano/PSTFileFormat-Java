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
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

/**
 * @author Tal Aloni <tal@kmrom.com>
 * @author Marc Bejerano <marcbejerano@gmail.com>
 */
public class ByteWriter {
    
    public static void putByte(byte[] buffer, int offset, byte value) {
        buffer[offset] = value;
    }

    public static void putBytes(byte[] buffer, int offset, byte[] value) {
        System.arraycopy(buffer, offset, value, 0, value.length);
    }

    /// <summary>
    /// Will return the ANSI string stored in the buffer
    /// </summary>
    public static void putAnsiString(byte[] buffer, int offset, String value) {
        throw new RuntimeException("Not implemented");
    }

    public static void putUTF16String(byte[] buffer, int offset, String value) {
        throw new RuntimeException("Not implemented");
    }

    public static void putNullTerminatedAnsiString(byte[] buffer, int offset, String value) {
        throw new RuntimeException("Not implemented");
    }

    public static void putNullTerminatedUTF16String(byte[] buffer, int offset, String value) {
        throw new RuntimeException("Not implemented");
    }

    public static void putBytes(OutputStream stream, int count, byte[] value) throws IOException {
        stream.write(value, 0, count);
    }

    /// <summary>
    /// Return all bytes from current stream position to the end of the stream
    /// </summary>
    public static void putAllBytes(OutputStream stream, byte[] value) throws IOException {
        putBytes(stream, value.length, value);
    }
}
