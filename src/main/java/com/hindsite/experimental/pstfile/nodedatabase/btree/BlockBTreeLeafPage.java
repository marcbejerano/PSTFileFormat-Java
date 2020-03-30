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

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tal Aloni <tal@kmrom.com>
 * @author Marc Bejerano <marcbejerano@gmail.com>
 */
public class BlockBTreeLeafPage extends BTreePage {

    public static final int MaximumNumberOfEntries = 20; // 488 / 24

    public List<BlockBTreeEntry> BlockEntryList = new ArrayList<>();

    public BlockBTreeLeafPage() {
        super();
    }

    public BlockBTreeLeafPage(byte[] buffer) {
        super(buffer);
    }

    @Override
    public void PopulateEntries(byte[] buffer, byte numberOfEntries) {
        for (int index = 0; index < numberOfEntries; index++) {
            int offset = index * cbEnt;
            BlockBTreeEntry entry = new BlockBTreeEntry(buffer, offset);
            BlockEntryList.add(entry);
        }
    }

    @Override
    public int WriteEntries(byte[] buffer) {
        int offset = 0;
        for (BlockBTreeEntry entry : BlockEntryList) {
            entry.WriteBytes(buffer, offset);
            offset += cbEnt;
        }
        return BlockEntryList.size();
    }

    private int GetSortedInsertIndex(BlockBTreeEntry entryToInsert) {
        long key = entryToInsert.BREF.bid.getValue();

        int insertIndex = 0;
        while (insertIndex < BlockEntryList.size() && key > BlockEntryList.get(insertIndex).BREF.bid.getValue()) {
            insertIndex++;
        }
        return insertIndex;
    }

    /// <returns>Insert index</returns>
    public int InsertSorted(BlockBTreeEntry entryToInsert) {
        int insertIndex = GetSortedInsertIndex(entryToInsert);
        BlockEntryList.add(insertIndex, entryToInsert);
        return insertIndex;
    }

    public int GetIndexOfEntry(long blockID) {
        for (int index = 0; index < BlockEntryList.size(); index++) {
            if (BlockEntryList.get(index).BREF.bid.getValue() == blockID) {
                return index;
            }
        }
        return -1;
    }

    public int RemoveEntry(long blockID) {
        int index = GetIndexOfEntry(blockID);
        if (index >= 0) {
            BlockEntryList.remove(index);
        }
        return index;
    }

    public BlockBTreeLeafPage Split() {
        int newNodeStartIndex = BlockEntryList.size() / 2;
        BlockBTreeLeafPage newPage = new BlockBTreeLeafPage();
        // BlockID will be given when the page will be added
        newPage.cEntMax = cEntMax;
        newPage.cbEnt = cbEnt;
        newPage.cLevel = cLevel;
        newPage.pageTrailer.ptype = pageTrailer.ptype;
        for (int index = newNodeStartIndex; index < BlockEntryList.size(); index++) {
            newPage.BlockEntryList.add(BlockEntryList.get(index));
        }

        BlockEntryList.removeAll(BlockEntryList.subList(newNodeStartIndex, BlockEntryList.size() - newNodeStartIndex));
        return newPage;
    }

    @Override
    public long getPageKey() {
        return BlockEntryList.get(0).BREF.bid.getValue();
    }
}
