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

import com.hindsite.experimental.pstfile.nodedatabase.PSTCRCCalculation;
import com.hindsite.experimental.utilities.byteutils.LittleEndianConverter;
import com.hindsite.experimental.utilities.byteutils.LittleEndianWriter;

/**
 *
 * @author marcb
 */
public class BlockTrailer {

    public static final int Length = 16;

    public short cb;
    public short wSig;
    public int dwCRC;
    public BlockID bid;

    public BlockTrailer() {
    }

    public BlockTrailer(byte[] buffer, int offset) {
        cb = LittleEndianConverter.getUInt16(buffer, offset + 0);
        wSig = LittleEndianConverter.getUInt16(buffer, offset + 2);
        dwCRC = LittleEndianConverter.getUInt32(buffer, offset + 4);
        bid = new BlockID(buffer, offset + 8);
    }
    
    public BlockTrailer(BlockTrailer copy) {
        this.cb = copy.cb;
        this.wSig = copy.wSig;
        this.dwCRC = copy.dwCRC;
        this.bid = new BlockID(copy.bid);
    }

    public void WriteBytes(byte[] buffer, int dataLength, int offset, long fileOffset) {
        wSig = ComputeSignature(fileOffset, bid.getValue());
        // CRC is only calculated on the raw data (i.e. excluding BlockTrailer and padding)
        dwCRC = PSTCRCCalculation.ComputeCRC(buffer, dataLength);

        LittleEndianWriter.putUInt16(buffer, offset + 0, cb);
        LittleEndianWriter.putUInt16(buffer, offset + 2, wSig);
        LittleEndianWriter.putUInt32(buffer, offset + 4, dwCRC);
        LittleEndianWriter.putUInt64(buffer, offset + 8, bid.getValue());
    }

    public static BlockTrailer ReadFromEndOfBuffer(byte[] buffer) {
        return new BlockTrailer(buffer, buffer.length - 16);
    }

    public static short ComputeSignature(long ib, long bid) {
        ib ^= bid;
        return ((short) ((short) (ib >> 16) ^ (short) (ib)));
    }
}
