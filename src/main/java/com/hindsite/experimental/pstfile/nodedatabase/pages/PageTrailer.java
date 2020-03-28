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
package com.hindsite.experimental.pstfile.nodedatabase.pages;

import com.hindsite.experimental.pstfile.nodedatabase.PSTCRCCalculation;
import com.hindsite.experimental.pstfile.nodedatabase.block.BlockID;
import com.hindsite.experimental.pstfile.nodedatabase.block.BlockTrailer;
import com.hindsite.experimental.pstfile.nodedatabase.enums.PageTypeName;
import com.hindsite.experimental.utilities.byteutils.LittleEndianConverter;
import com.hindsite.experimental.utilities.byteutils.LittleEndianWriter;

/**
 * @author Tal Aloni <tal@kmrom.com>
 * @author Marc Bejerano <marcbejerano@gmail.com>
 */
public class PageTrailer {

    public static final int Length = 16;
    public static final int OffsetFromPageStart = 496;

    public PageTypeName ptype;
    //public PageTypeName ptypeRepeat;
    public short wSig;
    public int dwCRC;
    public BlockID bid;

    public PageTrailer() {
    }

    public PageTrailer(byte[] buffer, int offset) {
        ptype = PageTypeName.valueOf(buffer[offset + 0]);
        PageTypeName ptypeRepeat = PageTypeName.valueOf(buffer[offset + 1]);
        wSig = LittleEndianConverter.getUInt16(buffer, offset + 2);
        dwCRC = LittleEndianConverter.getUInt32(buffer, offset + 4);
        bid = new BlockID(buffer, offset + 8);
    }

    public void WriteToPage(byte[] buffer, long fileOffset) {
        if (ptype == PageTypeName.ptypeBBT
                || ptype == PageTypeName.ptypeNBT
                || ptype == PageTypeName.ptypeDL) {
            wSig = BlockTrailer.ComputeSignature(fileOffset, bid.getValue());
        } else {
            wSig = 0;
            // AMap, PMap, FMap, and FPMap pages have a special convention where their BID is assigned the same value as their IB
            bid = new BlockID(fileOffset);
        }

        dwCRC = PSTCRCCalculation.ComputeCRC(buffer, OffsetFromPageStart);

        int offset = OffsetFromPageStart;
        buffer[offset + 0] = (byte) ptype.getValue();
        buffer[offset + 1] = (byte) ptype.getValue();
        LittleEndianWriter.putUInt16(buffer, offset + 2, wSig);
        LittleEndianWriter.putUInt32(buffer, offset + 4, dwCRC);
        LittleEndianWriter.putUInt64(buffer, offset + 8, bid.getValue());
    }

    public static PageTrailer ReadFromPage(byte[] buffer) {
        return new PageTrailer(buffer, OffsetFromPageStart);
    }
}
