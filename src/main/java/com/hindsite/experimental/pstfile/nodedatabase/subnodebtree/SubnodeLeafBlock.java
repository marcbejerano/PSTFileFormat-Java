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
package com.hindsite.experimental.pstfile.nodedatabase.subnodebtree;

import com.hindsite.experimental.pstfile.nodedatabase.block.Block;
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
public class SubnodeLeafBlock extends Block {

    public static final int MaximumNumberOfEntries = 340; // (8192 - 16 - 8) / 24

    public byte btype;
    public byte cLevel;
    // ushort cEnt;
    // dwPadding
    public List<SubnodeLeafEntry> rgentries = new ArrayList<>();

    public SubnodeLeafBlock() {
        super();
        btype = BlockType.SLBLOCK;
        cLevel = 0x00; // 0x00 for SLBLOCK
    }

    public SubnodeLeafBlock(byte[] buffer) {
        super(buffer);
        btype = buffer[0];
        cLevel = buffer[1];
        short cEnt = LittleEndianConverter.getUInt16(buffer, 2);

        int offset = 8;
        for (int index = 0; index < cEnt; index++) {
            SubnodeLeafEntry entry = new SubnodeLeafEntry(buffer, offset);
            rgentries.add(entry);
            offset += SubnodeLeafEntry.Length;
        }
    }
    
    public SubnodeLeafBlock(SubnodeLeafBlock copy) {
        super(copy);
        btype = copy.btype;
        cLevel = copy.cLevel;
        copy.rgentries.forEach(entry -> rgentries.add(new SubnodeLeafEntry(entry)));
    }

    @Override
    public void WriteDataBytes(byte[] buffer, int offset) {
        ByteWriter.putByte(buffer, offset + 0, (byte) btype);
        ByteWriter.putByte(buffer, offset + 1, cLevel);
        LittleEndianWriter.putInt32(buffer, offset + 2, rgentries.size());

        offset = 8;
        for (int index = 0; index < rgentries.size(); index++) {
            rgentries.get(index).WriteBytes(buffer, offset);
            offset += SubnodeLeafEntry.Length;
        }
    }

    public int IndexOfLeafEntry(int nodeID) {
        for (int index = 0; index < rgentries.size(); index++) {
            if (rgentries.get(index).nid.getValue() == nodeID) {
                return index;
            }
        }
        return -1;
    }

    public int GetSortedInsertIndex(SubnodeLeafEntry entryToInsert) {
        int key = entryToInsert.nid.getValue();

        int insertIndex = 0;
        while (insertIndex < rgentries.size() && key > rgentries.get(insertIndex).nid.getValue()) {
            insertIndex++;
        }
        return insertIndex;
    }

    /// <returns>Insert index</returns>
    public int InsertSorted(SubnodeLeafEntry entryToInsert) {
        int insertIndex = GetSortedInsertIndex(entryToInsert);
        rgentries.add(insertIndex, entryToInsert);
        return insertIndex;
    }

    public SubnodeLeafBlock Split() {
        int newBlockStartIndex = rgentries.size() / 2;
        SubnodeLeafBlock newBlock = new SubnodeLeafBlock();
        // BlockID will be given when the block will be added
        for (int index = newBlockStartIndex; index < rgentries.size(); index++) {
            newBlock.rgentries.add(rgentries.get(index));
        }

        rgentries.removeAll(rgentries.subList(newBlockStartIndex, rgentries.size() - newBlockStartIndex));
        return newBlock;
    }

    // Raw data contained in the block (excluding trailer and alignment padding)
    @Override
    public int getDataLength() {
        return 8 + rgentries.size() * 24;
    }

    public int getBlockKey() {
        return this.rgentries.get(0).nid.getValue();
    }
}
