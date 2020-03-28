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
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.UUID;

/**
 * @author Tal Aloni <tal@kmrom.com>
 * @author Marc Bejerano <marcbejerano@gmail.com>
 */
public class BigEndianConverter {

    public static short getInt16(byte[] buffer, int offset) {
        ByteBuffer bb = ByteBuffer.wrap(buffer);
        bb.order(ByteOrder.BIG_ENDIAN);
        return bb.getShort();
    }

    public static short getUInt16(byte[] buffer, int offset) {
        return getInt16(buffer, offset);
    }

    public static int getInt32(byte[] buffer, int offset) {
        ByteBuffer bb = ByteBuffer.wrap(buffer);
        bb.order(ByteOrder.BIG_ENDIAN);
        return bb.getInt();
    }

    public static int getUInt32(byte[] buffer, int offset) {
        return getInt32(buffer, offset);
    }

    public static long getInt64(byte[] buffer, int offset) {
        ByteBuffer bb = ByteBuffer.wrap(buffer);
        bb.order(ByteOrder.BIG_ENDIAN);
        return bb.getLong();
    }

    public static long getUInt64(byte[] buffer, int offset) {
        return getInt64(buffer, offset);
    }

    public static UUID getGuid(byte[] buffer, int offset) throws IOException {
        throw new RuntimeException("Not implemented");
    }
}
