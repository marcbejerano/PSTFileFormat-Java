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
package com.hindsite.experimental.pstfile.nodedatabase.btree;

import com.hindsite.experimental.pstfile.nodedatabase.block.BlockRef;
import com.hindsite.experimental.utilities.byteutils.LittleEndianConverter;
import com.hindsite.experimental.utilities.byteutils.LittleEndianWriter;

/**
 * @author Tal Aloni <tal@kmrom.com>
 * @author Marc Bejerano <marcbejerano@gmail.com>
 */
public class BlockBTreeEntry {

    public BlockRef BREF;
    public short cb;   // The count of bytes of the raw data contained in the block (excluding the block trailer and alignment padding)
    public short cRef; // Reference count: indicating the count of references to this block.
    //public uint dwPadding

    public BlockBTreeEntry() {
        BREF = new BlockRef();
    }

    public BlockBTreeEntry(byte[] buffer, int offset) {
        BREF = new BlockRef(buffer, offset + 0);
        cb = LittleEndianConverter.getUInt16(buffer, offset + 16);
        cRef = LittleEndianConverter.getUInt16(buffer, offset + 18);
    }

    public byte[] GetBytes() {
        byte[] buffer = new byte[24];
        WriteBytes(buffer, 0);
        return buffer;
    }

    public void WriteBytes(byte[] buffer, int offset) {
        BREF.WriteBytes(buffer, offset + 0);
        LittleEndianWriter.putUInt16(buffer, offset + 16, cb);
        LittleEndianWriter.putUInt16(buffer, offset + 18, cRef);
    }
}
