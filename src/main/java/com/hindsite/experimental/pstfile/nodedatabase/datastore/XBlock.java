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
package com.hindsite.experimental.pstfile.nodedatabase.datastore;

import com.hindsite.experimental.pstfile.nodedatabase.block.Block;
import com.hindsite.experimental.pstfile.nodedatabase.block.BlockID;
import com.hindsite.experimental.pstfile.nodedatabase.enums.BlockType;
import com.hindsite.experimental.utilities.byteutils.ByteWriter;
import com.hindsite.experimental.utilities.byteutils.LittleEndianConverter;
import com.hindsite.experimental.utilities.byteutils.LittleEndianWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Tal Aloni <tal@kmrom.com>
 * @author Marc Bejerano <marcbejerano@gmail.com>
 */
public class XBlock extends Block {

    public static final int MaximumNumberOfDataBlocks = 1021; // (8192 - 16 - 8) / 8
    public byte btype;
    public byte cLevel;
    //private ushort cEnt;
    public int lcbTotal; // Total bytes of all the external data
    public List<BlockID> rgbid = new ArrayList<>();

    public XBlock() {
        super();
        btype = BlockType.XBlock;
        cLevel = 0x01; // 0x01 for XBlock
    }

    public XBlock(byte[] buffer) {
        super(buffer);
        btype = buffer[0];
        cLevel = buffer[1];
        short cEnt = LittleEndianConverter.getUInt16(buffer, 2);
        lcbTotal = LittleEndianConverter.getUInt32(buffer, 4);
        int position = 8;
        for (int index = 0; index < cEnt; index++) {
            BlockID bid = new BlockID(buffer, position);
            rgbid.add(bid);
            position += 8;
        }
    }

    @Override
    public void WriteDataBytes(byte[] buffer, int offset) {
        ByteWriter.putByte(buffer, offset + 0, (byte) btype);
        ByteWriter.putByte(buffer, offset + 1, (byte) cLevel);
        LittleEndianWriter.putInt32(buffer, offset + 2, rgbid.size());
        LittleEndianWriter.putUInt32(buffer, offset + 4, lcbTotal);
        offset = 8;
        for (int index = 0; index < rgbid.size(); index++) {
            LittleEndianWriter.putUInt64(buffer, offset, rgbid.get(index).getValue());
            offset += 8;
        }
    }

    @Override
    public Block Clone() throws CloneNotSupportedException {
        XBlock result = (XBlock) this.clone();
        result.rgbid = new ArrayList<>();
        rgbid.forEach((blockID) -> {
            result.rgbid.add(blockID.Clone());
        });
        return result;
    }

    // Raw data contained in the block (excluding trailer and alignment padding)
    @Override
    public int getDataLength() {
        return 8 + rgbid.size() * 8;
    }

    public int getNumberOfDataBlocks() {
        return rgbid.size();
    }
}
