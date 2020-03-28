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
package com.hindsite.experimental.pstfile.nodedatabase.block;

import com.hindsite.experimental.utilities.byteutils.LittleEndianConverter;
import com.hindsite.experimental.utilities.byteutils.LittleEndianWriter;

/**
 * @author Tal Aloni <tal@kmrom.com>
 * @author Marc Bejerano <marcbejerano@gmail.com>
 */
public class BlockRef {

    public static final int Length = 16;

    public BlockID bid;
    public long ib; // byte offset from beginning of the file

    public BlockRef() {
    }

    public BlockRef(byte[] buffer, int offset) {
        bid = new BlockID(buffer, offset + 0);
        ib = LittleEndianConverter.getUInt64(buffer, offset + 8);
    }

    public void WriteBytes(byte[] buffer, int offset) {
        bid.WriteBytes(buffer, offset + 0);
        LittleEndianWriter.putUInt64(buffer, offset + 8, ib);
    }
}
