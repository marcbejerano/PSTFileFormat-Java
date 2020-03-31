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
package com.hindsite.pst;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import lombok.extern.java.Log;

/**
 * @author Marc Bejerano <marcbejerano@gmail.com>
 */
@Log
public class StreamUtils {
    
    public static void write(OutputStream out, byte[] array) throws IOException {
        log.finest(String.format("write %d bytes", array.length));
        out.write(array);
    }
    
    public static byte[] read(InputStream in, int count) throws IOException {
        byte[] buffer = new byte[count];
        in.read(buffer);
        return buffer;
    }

    public static void write(OutputStream out, int value) throws IOException {
        byte[] buffer = new byte[4]; // 32 bits
        ByteBuffer byteBuffer = ByteBuffer.wrap(buffer);
        byteBuffer.putInt(value);
        log.finest(String.format("write %d bytes", byteBuffer.array().length));
        out.write(byteBuffer.array());
    }

    public static int readInt(InputStream in) throws IOException {
        byte[] buffer = new byte[4]; // 32 bits
        in.read(buffer);
        ByteBuffer byteBuffer = ByteBuffer.wrap(buffer);
        return byteBuffer.getInt();
    }

    public static void write(OutputStream out, long value) throws IOException {
        byte[] buffer = new byte[8]; // 64 bits
        ByteBuffer byteBuffer = ByteBuffer.wrap(buffer);
        byteBuffer.putLong(value);
        log.finest(String.format("write %d bytes", byteBuffer.array().length));
        out.write(byteBuffer.array());
    }

    public static long readLong(InputStream in) throws IOException {
        byte[] buffer = new byte[8]; // 64 bits
        in.read(buffer);
        ByteBuffer byteBuffer = ByteBuffer.wrap(buffer);
        return byteBuffer.getLong();
    }
}
