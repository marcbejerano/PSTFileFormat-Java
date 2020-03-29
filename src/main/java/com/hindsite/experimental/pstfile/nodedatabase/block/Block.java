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
import com.hindsite.experimental.pstfile.nodedatabase.datastore.XBlock;
import com.hindsite.experimental.pstfile.nodedatabase.datastore.XXBlock;
import com.hindsite.experimental.pstfile.nodedatabase.enums.BCryptMethodName;
import com.hindsite.experimental.pstfile.nodedatabase.enums.BlockType;
import com.hindsite.experimental.pstfile.nodedatabase.exceptions.InvalidBlockIDException;
import com.hindsite.experimental.pstfile.nodedatabase.exceptions.InvalidChecksumException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Tal Aloni <tal@kmrom.com>
 * @author Marc Bejerano <marcbejerano@gmail.com>
 */
public abstract class Block {

    public static final int MaximumLength = 8192;

    public BlockTrailer blockTrailer;

    public Block() {
        blockTrailer = new BlockTrailer();
    }

    public Block(byte[] buffer) {
        blockTrailer = BlockTrailer.ReadFromEndOfBuffer(buffer);
    }

    public abstract void WriteDataBytes(byte[] buffer, int offset);

    public byte[] GetBytes(long fileOffset) {
        byte[] buffer = new byte[this.getTotalLength()];
        int paddingLength = getTotalLength() - getDataLength() - BlockTrailer.Length;
        blockTrailer.cb = (short) getDataLength();
        int offset = 0x00;
        WriteDataBytes(buffer, offset);
        offset += paddingLength;
        blockTrailer.WriteBytes(buffer, getDataLength(), offset, fileOffset);
        return buffer;
    }

    public static int GetTotalBlockLength(int dataLength) {
        // block is padded to 64-byte alignment
        int lengthWithTrailer = dataLength + BlockTrailer.Length;
        int length = (int) Math.ceil((double) lengthWithTrailer / 64) * 64;
        return length;
    }

    public static Block ReadFromStream(InputStream stream, BlockRef blockRef, int dataLength, BCryptMethodName bCryptMethod) throws Exception {
        long offset = (long) blockRef.ib;
        int totalLength = GetTotalBlockLength(dataLength);
//        stream.Seek(offset, SeekOrigin.Begin);
        byte[] buffer = new byte[totalLength];
        stream.read(buffer, 0, totalLength);

        BlockTrailer trailer = BlockTrailer.ReadFromEndOfBuffer(buffer);
        Block block;
        if (trailer.bid.isInternal()) {
            // XBlock or XXBlock
            byte btype = buffer[0];
            byte cLevel = buffer[1];
            if (btype == (byte) BlockType.XBlock && cLevel == 0x01) {
                // XBLOCK
                block = new XBlock(buffer);
            } else if (btype == (byte) BlockType.XXBlock && cLevel == 0x02) {
                // XXBLOCK
                block = new XXBlock(buffer);
            } else if (btype == (byte) BlockType.SLBLOCK && cLevel == 0x00) {
                // SLBLock
                block = new SubnodeLeafBlock(buffer);
            } else if (btype == (byte) BlockType.SIBLOCK && cLevel == 0x01) {
                // SIBLock
                block = new SubnodeIntermediateBlock(buffer);
            } else {
                throw new Exception("Internal block, but not XBLOCK, XXBlock, SLBLOCK or SIBLOCK");
            }
        } else {
            block = new DataBlock(buffer, bCryptMethod);
        }

        // See question 3 at:
        // http://social.msdn.microsoft.com/Forums/en-CA/os_binaryfile/thread/923f5964-4a89-4811-86c2-06a553c34510
        // However, so far all tests suggest that there should be no problem to use BlockID.Value for both arguments
        if (blockRef.bid.getLookupValue() != block.getBlockID().getLookupValue()) {
            throw new InvalidBlockIDException();
        }

        if (dataLength != trailer.cb) {
            throw new Exception("Invalid block length");
        }

        int crc = PSTCRCCalculation.ComputeCRC(buffer, dataLength);
        if (block.blockTrailer.dwCRC != crc) {
            throw new InvalidChecksumException();
        }

        int signature = BlockTrailer.ComputeSignature(blockRef.ib, blockRef.bid.getValue());
        if (block.blockTrailer.wSig != signature) {
            throw new InvalidChecksumException();
        }

        return block;
    }

    /// <summary>
    /// Block signature (wSig) will be set before writing
    /// </summary>
    public void WriteToStream(OutputStream stream, long offset) throws IOException {
        byte[] blockBytes = GetBytes((long) offset);
        //stream.Seek(offset, SeekOrigin.Begin);
        stream.write(blockBytes, 0, blockBytes.length);
    }

    public abstract Block Clone() throws CloneNotSupportedException;

    public BlockID getBlockID() {
        return blockTrailer.bid;
    }

    public void setBlockID(BlockID value) {
        this.blockTrailer.bid = value;
    }

    // Raw data contained in the block (excluding trailer and alignment padding)
    public abstract int getDataLength();

    public int getTotalLength() {
        return GetTotalBlockLength(this.getDataLength());
    }

}
