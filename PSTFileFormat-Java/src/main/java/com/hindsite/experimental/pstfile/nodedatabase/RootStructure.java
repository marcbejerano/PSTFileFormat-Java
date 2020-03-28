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
package com.hindsite.experimental.pstfile.nodedatabase;

import com.hindsite.experimental.pstfile.nodedatabase.block.BlockRef;
import com.hindsite.experimental.pstfile.nodedatabase.enums.WriterCompatibilityMode;
import com.hindsite.experimental.pstfile.nodedatabase.pages.AllocationMapPage;
import com.hindsite.experimental.utilities.byteutils.ByteReader;
import com.hindsite.experimental.utilities.byteutils.ByteWriter;
import com.hindsite.experimental.utilities.byteutils.LittleEndianConverter;
import com.hindsite.experimental.utilities.byteutils.LittleEndianWriter;

/**
 * @author Tal Aloni <tal@kmrom.com>
 * @author Marc Bejerano <marcbejerano@gmail.com>
 */
public class RootStructure {

    public static final int Length = 72;

    public static final byte INVALID_AMAP = 0x00;
    public static final byte VALID_AMAP1 = 0x01; // Outlook 2003
    public static final byte VALID_AMAP2 = 0x02; // Outlook 2007+

    public long ibFileEOF;
    public long ibAMapLast;
    public long cbAMapFree;
    public long cbPMapFree;
    public BlockRef BREFNBT; // Node BTree
    public BlockRef BREFBBT; // Block BTree
    public byte fAMapValid;
    // Outlook 2003-2010 use these value for implementation-specific data.
    // Modification of these values can result in failure to read the PST file by Outlook
    public byte bReserved;
    public short wReserved;

    public RootStructure(byte[] buffer, int offset) {
        ibFileEOF = LittleEndianConverter.getUInt64(buffer, offset + 4);
        ibAMapLast = LittleEndianConverter.getUInt64(buffer, offset + 12);
        cbAMapFree = LittleEndianConverter.getUInt64(buffer, offset + 20);
        cbPMapFree = LittleEndianConverter.getUInt64(buffer, offset + 28);
        BREFNBT = new BlockRef(buffer, offset + 36);
        BREFBBT = new BlockRef(buffer, offset + 52);
        fAMapValid = ByteReader.getByte(buffer, offset + 68);
        bReserved = ByteReader.getByte(buffer, offset + 69);
        wReserved = LittleEndianConverter.getUInt16(buffer, offset + 70);
    }

    public void WriteBytes(byte[] buffer, int offset, WriterCompatibilityMode writerCompatibilityMode) {
        if (fAMapValid == VALID_AMAP1 && writerCompatibilityMode.compareTo(WriterCompatibilityMode.Outlook2007RTM) >= 0) {
            fAMapValid = VALID_AMAP2;
        }

        LittleEndianWriter.putUInt64(buffer, offset + 4, ibFileEOF);
        LittleEndianWriter.putUInt64(buffer, offset + 12, ibAMapLast);
        LittleEndianWriter.putUInt64(buffer, offset + 20, cbAMapFree);
        LittleEndianWriter.putUInt64(buffer, offset + 28, cbPMapFree);
        BREFNBT.WriteBytes(buffer, offset + 36);
        BREFBBT.WriteBytes(buffer, offset + 52);
        ByteWriter.putByte(buffer, offset + 68, fAMapValid);
        ByteWriter.putByte(buffer, offset + 69, bReserved);
        LittleEndianWriter.putUInt16(buffer, offset + 70, wReserved);
    }

    public int getNumberOfAllocationMapPages() {
        return (int) ((ibAMapLast - AllocationMapPage.FirstPageOffset) / AllocationMapPage.MapppedLength) + 1;
    }

    public boolean IsAllocationMapValid() {
        return (fAMapValid == VALID_AMAP1 || fAMapValid == VALID_AMAP2);
    }

    public void setAllocationMapValue(boolean value) {
        if (value) {
            // We set it to VALID_AMAP2 during WriteBytes() if necessary
            fAMapValid = VALID_AMAP1;
        } else {
            fAMapValid = INVALID_AMAP;
        }
    }
}
