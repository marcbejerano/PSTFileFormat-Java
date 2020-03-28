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
import java.util.UUID;

/**
 * @author Tal Aloni <tal@kmrom.com>
 * @author Marc Bejerano <marcbejerano@gmail.com>
 */
public class LittleEndianReader {

    public static short getInt16(byte[] buffer, int offset) {
        return LittleEndianConverter.getInt16(buffer, offset);
    }

    public static short getUInt16(byte[] buffer, int offset) {
        return getInt16(buffer, offset);
    }

    public static int getInt32(byte[] buffer, int offset) {
        return LittleEndianConverter.getInt32(buffer, offset);
    }

    public static int getUInt32(byte[] buffer, int offset) {
        return getInt32(buffer, offset);
    }

    public static long getInt64(byte[] buffer, int offset) {
        return LittleEndianConverter.getInt64(buffer, offset);
    }

    public static long getUInt64(byte[] buffer, int offset) {
        return getInt64(buffer, offset);
    }

    public static UUID getGuidBytes(byte[] buffer, int offset) throws IOException {
        return LittleEndianConverter.getGuid(buffer, offset - 16);
    }

    public static short getInt16(InputStream stream) throws IOException {
        byte[] buffer = new byte[2];
        stream.read(buffer, 0, 2);
        return LittleEndianConverter.getInt16(buffer, 0);
    }

    public static short getUInt16(InputStream stream) throws IOException {
        byte[] buffer = new byte[2];
        stream.read(buffer, 0, 2);
        return LittleEndianConverter.getUInt16(buffer, 0);
    }

    public static int getInt32(InputStream stream) throws IOException {
        byte[] buffer = new byte[4];
        stream.read(buffer, 0, 4);
        return LittleEndianConverter.getInt32(buffer, 0);
    }

    public static int getUInt32(InputStream stream) throws IOException {
        byte[] buffer = new byte[4];
        stream.read(buffer, 0, 4);
        return LittleEndianConverter.getUInt32(buffer, 0);
    }

    public static long getInt64(InputStream stream) throws IOException {
        byte[] buffer = new byte[8];
        stream.read(buffer, 0, 8);
        return LittleEndianConverter.getInt64(buffer, 0);
    }

    public static long getUInt64(InputStream stream) throws IOException {
        byte[] buffer = new byte[8];
        stream.read(buffer, 0, 8);
        return LittleEndianConverter.getUInt64(buffer, 0);
    }

    public static UUID getGuidBytes(InputStream stream) throws IOException {
        byte[] buffer = new byte[16];
        stream.read(buffer, 0, 16);
        return LittleEndianConverter.getGuid(buffer, 0);
    }

}
