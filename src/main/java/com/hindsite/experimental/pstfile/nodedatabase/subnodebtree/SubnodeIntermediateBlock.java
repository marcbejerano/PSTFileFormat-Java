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
public class SubnodeIntermediateBlock extends Block {

    public static final int MaximumNumberOfEntries = 510; // (8192 - 16 - 8) / 16

    public byte btype;
    public byte cLevel;
    //private ushort cEnt;
    // dwPadding
    public List<SubnodeIntermediateEntry> rgentries = new ArrayList<>();

    public SubnodeIntermediateBlock() {
        super();
        btype = BlockType.SIBLOCK;
        cLevel = 0x01; // 0x01 for SIBLOCK
    }

    public SubnodeIntermediateBlock(byte[] buffer) {
        super(buffer);

        btype = buffer[0];
        cLevel = buffer[1];
        short cEnt = LittleEndianConverter.getUInt16(buffer, 2);

        int offset = 8;
        for (int index = 0; index < cEnt; index++) {
            SubnodeIntermediateEntry entry = new SubnodeIntermediateEntry(buffer, offset);
            rgentries.add(entry);
            offset += SubnodeIntermediateEntry.Length;
        }
    }

    public SubnodeIntermediateBlock(SubnodeIntermediateBlock copy) {
        btype = copy.btype;
        cLevel = copy.cLevel;
        copy.rgentries.forEach(entry -> rgentries.add(new SubnodeIntermediateEntry(entry)));
    }

    @Override
    public void WriteDataBytes(byte[] buffer, int offset) {
        ByteWriter.putByte(buffer, offset + 0, (byte) btype);
        ByteWriter.putByte(buffer, offset + 1, cLevel);
        LittleEndianWriter.putInt32(buffer, offset + 2, rgentries.size());
        offset = 8;
        for (int index = 0; index < rgentries.size(); index++) {
            rgentries.get(index).WriteBytes(buffer, offset);
            offset += SubnodeIntermediateEntry.Length;
        }
    }

    /// <summary>
    /// Find the index of the SubnodeIntermediateEntry pointing to the SubnodeLeafBlock where the
    /// entry with the given key should be located
    /// </summary>
    public int IndexOfIntermediateEntryWithMatchingRange(int nodeID) {
        int lastIndexToMatch = 0;
        for (int index = 1; index < rgentries.size(); index++) {
            // All the entries in the child have key values greater than or equal to this key value.
            if (rgentries.get(index).nid.getValue() <= nodeID) {
                lastIndexToMatch = index;
            }
        }
        return lastIndexToMatch;
    }

    public int GetIndexOfBlockID(long blockID) {
        for (int index = 0; index < rgentries.size(); index++) {
            if (rgentries.get(index).bid.getValue() == blockID) {
                return index;
            }
        }
        return -1;
    }

    public int GetSortedInsertIndex(SubnodeIntermediateEntry entryToInsert) {
        int key = entryToInsert.nid.getValue();

        int insertIndex = 0;
        while (insertIndex < rgentries.size() && key > rgentries.get(insertIndex).nid.getValue()) {
            insertIndex++;
        }
        return insertIndex;
    }

    /// <returns>Insert index</returns>
    public int InsertSorted(SubnodeIntermediateEntry entryToInsert) {
        int insertIndex = GetSortedInsertIndex(entryToInsert);
        rgentries.add(insertIndex, entryToInsert);
        return insertIndex;
    }

    // Raw data contained in the block (excluding trailer and alignment padding)
    @Override
    public int getDataLength() {
        return 8 + rgentries.size() * 16;
    }
}
