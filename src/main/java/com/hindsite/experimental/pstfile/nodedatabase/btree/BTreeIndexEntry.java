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
public class BTreeIndexEntry {

    public static final int Length = 24;

    public long btkey;   // All the entries in the child BTPAGE referenced by BREF have key values greater than or equal to this key value.
    public BlockRef BREF;

    public BTreeIndexEntry() {
        BREF = new BlockRef();
    }

    public BTreeIndexEntry(byte[] buffer, int offset) {
        btkey = LittleEndianConverter.getUInt64(buffer, offset + 0);
        BREF = new BlockRef(buffer, offset + 8);
    }

    public void WriteBytes(byte[] buffer, int offset) {
        LittleEndianWriter.putUInt64(buffer, offset + 0, btkey);
        BREF.WriteBytes(buffer, offset + 8);
    }
}
