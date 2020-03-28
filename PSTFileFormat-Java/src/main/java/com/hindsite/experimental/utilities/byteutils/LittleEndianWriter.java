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
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.UUID;

/**
 * @author Tal Aloni <tal@kmrom.com>
 * @author Marc Bejerano <marcbejerano@gmail.com>
 */
public class LittleEndianWriter {
    
    public static void putInt16(byte[] buffer, int offset, short value) {
        ByteBuffer bb = ByteBuffer.wrap(buffer);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        bb.putShort(offset, value);
    }

    public static void putUInt16(byte[] buffer, int offset, short value) {
        putInt16(buffer, offset, value);
    }

    public static void putInt32(byte[] buffer, int offset, int value) {
        ByteBuffer bb = ByteBuffer.wrap(buffer);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        bb.putInt(offset, value);
    }

    public static void putUInt32(byte[] buffer, int offset, int value) {
        putInt32(buffer, offset, value);
    }

    public static void putInt64(byte[] buffer, int offset, long value) {
        ByteBuffer bb = ByteBuffer.wrap(buffer);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        bb.putLong(offset, value);
    }

    public static void putUInt64(byte[] buffer, int offset, long value) {
        putInt64(buffer, offset, value);
    }

    public static void putGuidBytes(byte[] buffer, int offset, UUID value) {
        throw new RuntimeException("Not implemented");
    }

    public static void putInt16(OutputStream stream, short value) throws IOException {
        ByteBuffer bb = ByteBuffer.allocate(2);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        bb.putShort(value);
        stream.write(bb.array());
    }

    public static void putUInt16(OutputStream stream, short value) throws IOException {
        putInt16(stream, value);
    }

    public static void putInt32(OutputStream stream, int value) throws IOException {
        ByteBuffer bb = ByteBuffer.allocate(4);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        bb.putInt(value);
        stream.write(bb.array());
    }

    public static void putUInt32(OutputStream stream, int value) throws IOException {
        putInt32(stream, value);
    }

    public static void putInt64(OutputStream stream, long value) throws IOException {
        ByteBuffer bb = ByteBuffer.allocate(8);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        bb.putLong(value);
        stream.write(bb.array());
    }

    public static void putUInt64(OutputStream stream, long value) throws IOException {
        putInt64(stream, value);
    }

    public static void putGuidBytes(OutputStream stream, UUID value) {
        throw new RuntimeException("Not implemented");
    }

}
